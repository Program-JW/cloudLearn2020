package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2020/08/14 17:09
 */
@AllArgsConstructor
public enum EnvelopeChannelEnum {

    /**
     * 消息中心 - 消息渠道枚举
     */

    WORKFLOW(1, "推推棒"),

    SPECIAL_AFTER_SALE(2, "特殊售后审批"),

    ;

    @Getter
    private final int code;
    @Getter
    private final String value;
}
