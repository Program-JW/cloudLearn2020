package com.ruigu.rbox.workflow.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author caojinghong
 * @date 2020-02-19 13:09
 */
@Component
@Slf4j
public class NotifyConfirmCallback implements RabbitTemplate.ConfirmCallback {

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        long msgId = Long.parseLong(Objects.requireNonNull(correlationData.getId()));
        if (ack) {
            log.info("消息:{},发送成功", msgId);
        } else {
            log.error("消息:{},发送失败,原因:{}", correlationData.toString(), cause);
        }
    }
}
