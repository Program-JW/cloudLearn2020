package com.ruigu.rbox.workflow.mq;

import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.config.RabbitMqConfig;
import com.ruigu.rbox.workflow.model.dto.BaseNotifyDTO;
import com.ruigu.rbox.workflow.model.dto.MsgNotifyDTO;
import com.ruigu.rbox.workflow.model.entity.RabbitmqMsgLogEntity;
import com.ruigu.rbox.workflow.service.RabbitmqMsgService;
import com.ruigu.rbox.workflow.strategy.context.ChatMqMsgHandleContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author liqingtian
 * @date 2020/02/04 9:25
 */
@Slf4j
@Service
public class RabbitChatMsgListener {

    @Resource
    private RabbitmqMsgService rabbitmqMsgService;

    @Resource
    private ChatMqMsgHandleContext chatMqMsgHandleContext;

    @RabbitListener(queues = RabbitMqConfig.CHAT_QUEUE_NAME)
    public void process(Message message) {
        log.info("=============================== 收到新的 聊天室 Rabbit MQ 消息 ================================");
        String messageString = new String(message.getBody());
        RabbitmqMsgLogEntity rabbitmqMsg = new RabbitmqMsgLogEntity();
        rabbitmqMsg.setMessage(messageString);
        try {
            BaseNotifyDTO baseNotifyDTO = JsonUtil.parseObject(messageString, BaseNotifyDTO.class);
            String content = baseNotifyDTO.getContent();
            if (StringUtils.isBlank(content)) {
                log.error("| - [ 聊天室 rabbit mq 监听 ] 消息体content为空");
                return;
            }
            MsgNotifyDTO msgNotifyDTO = JsonUtil.parseObject(content, MsgNotifyDTO.class);
            if (msgNotifyDTO != null) {
                rabbitmqMsg.setTaskIdWeixin(msgNotifyDTO.getMsgId().toString());
                chatMqMsgHandleContext.handle(baseNotifyDTO.getAction(), msgNotifyDTO);
            }
        } catch (Exception e) {
            log.error("| - [ 聊天室 rabbit mq 监听 ] 消息类型转换失败");
        } finally {
            rabbitmqMsgService.saveMsg(rabbitmqMsg);
        }
    }
}
