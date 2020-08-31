package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2020/08/14 16:49
 */
@AllArgsConstructor
public enum SpecialAfterSaleApplyStatusEnum {

    UNDO(-1, "撤销"),

    ING(0, "审批中"),

    PASS(1, "通过"),

    REJECT(2, "驳回"),

    ;

    @Getter
    private final int code;

    @Getter
    private final String desc;
}
