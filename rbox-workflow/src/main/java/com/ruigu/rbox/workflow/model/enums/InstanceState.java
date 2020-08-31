package com.ruigu.rbox.workflow.model.enums;

import lombok.Getter;

/**
 * @author alan.zhao
 */
public enum InstanceState {
    /**
     * 未处理
     */
    PENDING_START(0, "待启动"),

    /**
     * 运行中
     */
    RUNNING(1, "运行中"),

    /**
     * 暂停
     */
    SLEEP(2, "暂停"),

    /**
     * 作废
     */
    INVALID(-1, "作废");

    @Getter
    private final int state;

    @Getter
    private final String desc;

    InstanceState(int state, String desc) {
        this.state = state;
        this.desc = desc;
    }
}
