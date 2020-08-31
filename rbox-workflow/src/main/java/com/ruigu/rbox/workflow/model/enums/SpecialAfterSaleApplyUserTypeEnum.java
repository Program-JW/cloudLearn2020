package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2020/08/12 20:36
 */
@AllArgsConstructor
public enum SpecialAfterSaleApplyUserTypeEnum {

    BD(1),
    DX(2),
    ;

    @Getter
    private final int code;
}
