package com.ruigu.rbox.workflow.model.enums;

import lombok.Getter;

/**
 * @author liqingtian
 * @date 2019/10/12 13:58
 */
public enum TaskState {

    /**
     * 未处理
     */
    UNTREATED(0, "未处理"),

    /**
     * 运行中
     */
    RUNNING(1, "处理中"),

    /**
     * 暂停
     */
    COMPLETED(2, "处理完成"),

    /**
     * 批准
     */
    APPROVAL(3, "批准"),

    /**
     * 驳回
     */
    REJECT(4, "驳回"),

    /**
     * 作废
     */
    INVALID(-1, "作废");

    @Getter
    private final int state;

    @Getter
    private final String desc;

    TaskState(int state, String desc) {
        this.state = state;
        this.desc = desc;
    }
}
