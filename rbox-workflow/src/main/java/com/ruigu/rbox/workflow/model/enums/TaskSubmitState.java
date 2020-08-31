package com.ruigu.rbox.workflow.model.enums;

import lombok.Getter;

/**
 * @author liqingtian
 * @date 2019/11/11 16:55
 */
public enum TaskSubmitState {

    /**
     * 批准
     */
    PASS(1, "批准"),

    /**
     * 驳回
     */
    REJECT(0, "驳回");

    @Getter
    private int code;

    @Getter
    private String text;

    TaskSubmitState(int code, String text) {
        this.code = code;
        this.text = text;
    }
}
