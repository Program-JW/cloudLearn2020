package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2020/07/10 16:01
 */
@AllArgsConstructor
public enum ExchangeTypeEnum {

    /**
     * mq 交换机类型
     */

    FANOUT("fanout"),

    DIRECT("direct"),

    TOPIC("topic"),

    DEFAULT(""),

    ;

    @Getter
    private final String type;
}
