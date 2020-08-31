package com.ruigu.rbox.workflow.model.enums;

import lombok.Getter;

/**
 * @author alan.zhao
 */
public enum WorkOrderState {
    /**
     * 待处理
     */
    TO_BE_PROCESSED(0, "待处理"),

    /**
     * 处理中
     */
    PROCESSING(1, "处理中"),

    /**
     * 已关闭
     */
    CLOSED(2, "已关闭"),

    /**
     * 已作废
     */
    INVALID(-1, "已作废");

    @Getter
    private final int state;

    @Getter
    private final String desc;

    WorkOrderState(int state, String desc) {
        this.state = state;
        this.desc = desc;
    }
}
