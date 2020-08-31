package com.ruigu.rbox.workflow.model.enums;

import lombok.Getter;

/**
 * @author liqingtian
 * @date 2019/10/10 14:52
 */
public enum SqlConditionType {
    /**
     * 相等
     */
    EQUALS(1),

    /**
     * like
     */
    LIKE(2);

    @Getter
    private final int code;

    SqlConditionType(int code) {
        this.code = code;
    }
}
