package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.workflow.constants.RedisKeyConstants;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.model.entity.ReliableMqLogEntity;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.enums.TxMessageStatusEnum;
import com.ruigu.rbox.workflow.repository.GuaranteeSuccessRabbitmqMessageRepository;
import com.ruigu.rbox.workflow.service.GuaranteeSuccessMqMessageService;
import com.ruigu.rbox.workflow.service.QuestNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author liqingtian
 * @date 2020/07/21 10:32
 */
@Slf4j
@Service
public class GuaranteeSuccessMqMessageServiceImpl implements GuaranteeSuccessMqMessageService {

    private static final LocalDateTime END = LocalDateTime.of(2999, 1, 1, 0, 0, 0);

    private static final long DEFAULT_INIT_BACKOFF = 10L;

    private static final int DEFAULT_BACKOFF_FACTOR = 2;

    private static final int DEFAULT_MAX_RETRY_TIMES = 5;

    @Resource
    private GuaranteeSuccessRabbitmqMessageRepository guaranteeSuccessRabbitmqMessageRepository;

    @Resource(name = "guaranteeSuccessRabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    @Resource(name = "rboxGuaranteeSuccessAmqpAdmin")
    private AmqpAdmin amqpAdmin;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private QuestNoticeService questNoticeService;

    /**
     * exchange 回调
     */
    private final RabbitTemplate.ConfirmCallback confirmCallback = (correlationData, ack, cause) -> {

        // 更新失败状态
        String uuid = correlationData == null ? "null" : correlationData.getId();

        // redis中查找
        String recordIdStr = stringRedisTemplate.opsForValue().get(String.format(RedisKeyConstants.MQ_ID_UUID_CORRELATION_KEY, uuid));
        Long recordId = Long.valueOf(recordIdStr);

        if (!ack) {

            log.error("消息发送交换机失败");
            log.error("correlationData : {}", correlationData);
            log.error("失败原因 : {}", cause);

            // 更新失败状态
            markFail(recordId);

            // 发消息
            questNoticeService.sendWarnNotice("推推棒MQ消息发送异常\n" +
                    "发送MQ消息 - 发送交换机失败\n" +
                    "record ID：" + recordId + "\n" +
                    "原因：" + cause
            );
        } else {

            // 更新成功状态
            markSuccess(recordId);
        }
    };

    /**
     * 队列回调
     */
    private final RabbitTemplate.ReturnCallback returnCallback = (message, replyCode, replyText, exchange, routingKey) -> {

        log.error("消息发送队列失败");
        log.error("returnedMessage : {}", message);
        log.error("exchange : {}", exchange);
        log.error("routingKey : {}", routingKey);

        // 发消息
        questNoticeService.sendWarnNotice("推推棒MQ消息发送异常\n" +
                "发送MQ消息 - 发送队列失败\n" +
                "exchange：" + exchange + "\n" +
                "routingKey：" + routingKey + "\n" +
                "returnedMessage" + message);
    };

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecord(ReliableMqLogEntity record) {
        record.setMessageStatus(TxMessageStatusEnum.PENDING.getStatus());
        LocalDateTime now = LocalDateTime.now();
        record.setNextScheduleTime(calculateNextScheduleTime(now, DEFAULT_INIT_BACKOFF, DEFAULT_BACKOFF_FACTOR, 0));
        record.setCurrentRetryTimes(0);
        record.setInitBackoff(DEFAULT_INIT_BACKOFF);
        record.setBackoffFactor(DEFAULT_BACKOFF_FACTOR);
        record.setMaxRetryTimes(DEFAULT_MAX_RETRY_TIMES);
        record.setCreatedAt(now);
        record.setLastUpdatedAt(now);
        Integer userId = UserHelper.getUserId() == null ? 0 : UserHelper.getUserId();
        record.setCreatedBy(userId);
        record.setLastUpdatedBy(userId);
        guaranteeSuccessRabbitmqMessageRepository.save(record);
    }

    @Override
    public void send(ReliableMqLogEntity record) {
        amqpAdmin.declareExchange(new TopicExchange(record.getExchangeName()));

        // 确认机制
        rabbitTemplate.setConfirmCallback(confirmCallback);

        // 回调
        rabbitTemplate.setReturnCallback(returnCallback);

        // 若想无侵入式的实现则 需要将uuid引入，并建立uuid与mqLogId之间的关系
        String uuid = UUID.randomUUID().toString();

        // 将关系存入redis
        stringRedisTemplate.opsForValue().set(String.format(RedisKeyConstants.MQ_ID_UUID_CORRELATION_KEY, uuid),
                record.getId().toString(), 10, TimeUnit.MINUTES);

        // 发送消息
        rabbitTemplate.convertAndSend(record.getExchangeName(), record.getRoutingKey(), record.getContent(), new CorrelationData(uuid));
    }

    @Override
    public void scanRecordAndRetry() {

        // 该方法主要用于失败重试
        // 失败包括两种场景 1.已发送 发送失败 2.未发送
        List<ReliableMqLogEntity> notSuccessRecords = guaranteeSuccessRabbitmqMessageRepository.findAllNeedRetryRecord(
                Arrays.asList(TxMessageStatusEnum.PENDING.getStatus(), TxMessageStatusEnum.FAIL.getStatus()));
        if (CollectionUtils.isEmpty(notSuccessRecords)) {
            return;
        }
        for (ReliableMqLogEntity record : notSuccessRecords) {
            if (TxMessageStatusEnum.PENDING.getStatus().equals(record.getMessageStatus())) {
                // 对于未发送的记录 应判断是否是新建的 因此判断时候新建已10分钟 再发
                if (record.getCreatedAt().plusMinutes(10).isAfter(LocalDateTime.now())) {
                    return;
                }
            }
            // 发送 - 重试
            send(record);
        }

    }

    @Override
    public void failRecordManualRetry(List<Long> recordIds, Boolean maxLimit) {

        List<ReliableMqLogEntity> retryRecord = new ArrayList<>();


        if (CollectionUtils.isEmpty(retryRecord)) {
            throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "重试记录为空");
        }

    }


    private LocalDateTime calculateNextScheduleTime(LocalDateTime base,
                                                    long initBackoff,
                                                    long backoffFactor,
                                                    long round) {
        double delta = initBackoff * Math.pow(backoffFactor, round);
        return base.plusSeconds((long) delta);
    }

    private void markSuccess(Long recordId) {
        ReliableMqLogEntity record = guaranteeSuccessRabbitmqMessageRepository.findById(recordId).orElse(null);
        if (record == null) {
            log.error(" mark success ! but record id is lost , id:{}", recordId);
            return;
        }
        // 标记下一次执行时间为最大值
        record.setNextScheduleTime(END);
        record.setCurrentRetryTimes(record.getCurrentRetryTimes().compareTo(record.getMaxRetryTimes()) >= 0 ?
                record.getMaxRetryTimes() : record.getCurrentRetryTimes() + 1);
        record.setMessageStatus(TxMessageStatusEnum.SUCCESS.getStatus());
        record.setLastUpdatedAt(LocalDateTime.now());
        guaranteeSuccessRabbitmqMessageRepository.save(record);
    }

    private void markFail(Long recordId) {
        ReliableMqLogEntity record = guaranteeSuccessRabbitmqMessageRepository.findById(recordId).orElse(null);
        if (record == null) {
            log.error(" mark success ! but record id is lost , id:{}", recordId);
            return;
        }
        record.setCurrentRetryTimes(record.getCurrentRetryTimes().compareTo(record.getMaxRetryTimes()) >= 0 ?
                record.getMaxRetryTimes() : record.getCurrentRetryTimes() + 1);
        // 计算下一次的执行时间
        LocalDateTime nextScheduleTime = calculateNextScheduleTime(
                record.getNextScheduleTime(),
                record.getInitBackoff(),
                record.getBackoffFactor(),
                record.getCurrentRetryTimes()
        );
        record.setNextScheduleTime(nextScheduleTime);
        record.setMessageStatus(TxMessageStatusEnum.FAIL.getStatus());
        record.setLastUpdatedAt(LocalDateTime.now());
        guaranteeSuccessRabbitmqMessageRepository.save(record);
    }
}
