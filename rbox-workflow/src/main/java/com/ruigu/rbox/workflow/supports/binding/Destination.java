package com.ruigu.rbox.workflow.supports.binding;


import com.ruigu.rbox.workflow.model.enums.ExchangeTypeEnum;

/**
 * @author liqingtian
 * @date 2020/07/10 16:09
 */
public interface Destination {

    ExchangeTypeEnum exchangeType();

    String queueName();

    String exchangeName();

    String routingKey();

}
