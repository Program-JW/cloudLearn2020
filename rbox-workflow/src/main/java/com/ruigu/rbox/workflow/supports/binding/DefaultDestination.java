package com.ruigu.rbox.workflow.supports.binding;

import com.ruigu.rbox.workflow.model.enums.ExchangeTypeEnum;
import lombok.Builder;

/**
 * @author liqingtian
 * @date 2020/07/10 16:11
 */
@Builder
public class DefaultDestination implements Destination {

    private ExchangeTypeEnum exchangeType;

    private String queueName;

    private String exchangeName;

    private String routingKey;

    @Override
    public ExchangeTypeEnum exchangeType() {
        return exchangeType;
    }

    @Override
    public String queueName() {
        return queueName;
    }

    @Override
    public String exchangeName() {
        return exchangeName;
    }

    @Override
    public String routingKey() {
        return routingKey;
    }
}
