package com.ruigu.rbox.workflow.strategy.context;

import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.config.RabbitMqConfig;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.entity.NoticeTemplateEntity;
import com.ruigu.rbox.workflow.model.enums.NoticeType;
import com.ruigu.rbox.workflow.model.request.EnvelopeReq;
import com.ruigu.rbox.workflow.model.vo.MessageInfoVO;
import com.ruigu.rbox.workflow.strategy.BuildNoticeStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/10/14 17:50
 */
@Slf4j
@Component
public class SendNoticeContext {

    @Resource
    private Map<String, BuildNoticeStrategy> map;

    @Resource(name = "rabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RabbitMqConfig.SendProperties sendProperties;

    public ServerResponse send(NoticeTemplateEntity template, MessageInfoVO message) {
        String type = null;
        if (template.getType() != null) {
            type = NoticeType.getDesc(template.getType());
        } else {
            type = NoticeType.getDesc(template.getChannel());
        }
        if (StringUtils.isNotBlank(type)) {
            if (map.containsKey(type)) {
                try {
                    EnvelopeReq build = map.get(type).build(message);
                    rabbitTemplate.convertAndSend(sendProperties.getExchange(), sendProperties.getRouting(), JsonUtil.toJsonString(build));
                    return ServerResponse.ok();
                } catch (Exception e) {
                    log.error("| -- 消息发送异常 ： e - {} ", e);
                }
            }
        }
        return ServerResponse.fail();
    }
}
