package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.supports.binding.Destination;
import com.ruigu.rbox.workflow.supports.message.TxMessage;

/**
 * 保证成功的mq发送者
 *
 * @author liqingtian
 * @date 2020/07/21 10:00
 */
public interface GuaranteeSuccessMqSender {

    /**
     * 消息发送
     *
     * @param destination mq 路由，队列信息
     * @param message     mq 消息信息
     */
    void send(Destination destination, TxMessage message);
}
