package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2020/08/12 15:36
 */
@AllArgsConstructor
public enum YesOrNoOrDefaultEnum {

    DEFAULT(0),

    YES(1),

    NO(2);

    @Getter
    private final int code;
}
