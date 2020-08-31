package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2020/07/29 9:56
 */
@AllArgsConstructor
public enum EnvelopeTypeEnum {

    /**
     * 消息中心 - 消息类型枚举
     */

    EMAIL(1, ""),

    WEIXIN_TEXT(2,"text"),

    WEIXIN_TASK_CARD(3,"taskCard"),

    WEIXIN_TEXT_CARD(4,"textCard");

    @Getter
    private final int code;

    @Getter
    private final String value;
}
