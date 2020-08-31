package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.workflow.model.entity.ReliableMqLogEntity;
import com.ruigu.rbox.workflow.model.enums.ExchangeTypeEnum;
import com.ruigu.rbox.workflow.service.GuaranteeSuccessMqMessageService;
import com.ruigu.rbox.workflow.service.GuaranteeSuccessMqSender;
import com.ruigu.rbox.workflow.supports.binding.Destination;
import com.ruigu.rbox.workflow.supports.message.TxMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;

/**
 * @author liqingtian
 * @date 2020/07/21 10:08
 */
@Service
public class GuaranteeSuccessMqSendServiceImpl implements GuaranteeSuccessMqSender {

    @Resource
    private GuaranteeSuccessMqMessageService guaranteeSuccessMqMessageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void send(Destination destination, TxMessage message) {
        String exchangeName = destination.exchangeName();
        String routingKey = destination.routingKey();
        ExchangeTypeEnum exchangeType = destination.exchangeType();

        ReliableMqLogEntity record = new ReliableMqLogEntity();
        record.setExchangeName(exchangeName);
        record.setExchangeType(exchangeType.getType());
        record.setRoutingKey(routingKey);
        record.setBusinessModule(message.businessModule());
        record.setBusinessKey(message.businessKey());
        record.setContent(message.content());

        // 保存事务消息记录
        guaranteeSuccessMqMessageService.saveRecord(record);

        // 事务异步执行器 （可控制在事务提交后执行）
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                guaranteeSuccessMqMessageService.send(record);
            }
        });
    }
}
