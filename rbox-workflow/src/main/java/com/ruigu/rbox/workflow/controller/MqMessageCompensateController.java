package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.enums.ExchangeTypeEnum;
import com.ruigu.rbox.workflow.service.GuaranteeSuccessMqSender;
import com.ruigu.rbox.workflow.supports.binding.DefaultDestination;
import com.ruigu.rbox.workflow.supports.message.DefaultTxMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liqingtian
 * @date 2020/07/21 19:29
 */
@RestController
public class MqMessageCompensateController {

    @Resource
    private GuaranteeSuccessMqSender guaranteeSuccessMqSender;

    @GetMapping("/test/send/mq")
    public ServerResponse testMqSend() {
        guaranteeSuccessMqSender.send(
                DefaultDestination.builder()
                        .exchangeType(ExchangeTypeEnum.TOPIC).
                        exchangeName("exchangeName")
                        .queueName("queueName")
                        .routingKey("routingKey")
                        .build(),
                DefaultTxMessage.builder()
                        .businessModule("businessModule")
                        .businessKey("businessKey")
                        .content("content")
                        .build());
        return ServerResponse.ok();
    }
}
