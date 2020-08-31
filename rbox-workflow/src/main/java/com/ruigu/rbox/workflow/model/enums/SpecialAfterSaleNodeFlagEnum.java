package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2020/08/12 15:45
 */
@AllArgsConstructor
public enum SpecialAfterSaleNodeFlagEnum {

    POSITION(1, "职位"),

    PERSON(2, "单人"),
    ;

    @Getter
    private final int code;
    @Getter
    private final String value;
}
