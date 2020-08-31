package com.ruigu.rbox.workflow.model.enums;

import lombok.Getter;

/**
 * @author alan.zhao
 */
public enum CandidateUserType {
    /**
     * 创建人
     */
    CREATOR(-1, "创建人"),

    /**
     * 创建人上级
     */
    CREATOR_LEADER(-2, "创建人上级");

    @Getter
    private final int state;

    @Getter
    private final String desc;

    CandidateUserType(int state, String desc) {
        this.state = state;
        this.desc = desc;
    }
}
