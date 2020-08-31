package com.ruigu.rbox.workflow.model.enums;

import lombok.Getter;

/**
 * @author panjianwei
 * @date 2020/08/12 13:53
 */
public enum SpecialAfterSaleApproverTypeEnum {

    /**
     * 职位
     */
    POSITION(1, "职位"),
    /**
     * 单人
     */
    SINGLE(2, "单人");

    @Getter
    private final int state;

    @Getter
    private final String mean;

    SpecialAfterSaleApproverTypeEnum(int state, String mean) {
        this.state = state;
        this.mean = mean;
    }
}
