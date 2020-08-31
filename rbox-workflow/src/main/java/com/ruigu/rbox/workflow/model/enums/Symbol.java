package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2019/12/31 17:03
 */

@AllArgsConstructor
public enum Symbol {

    /**
     * 标点符号
     */
    COMMA(","),
    UNDERLINE("_"),
    HYPHEN("-"),
    COLON(":"),
    BR("\n");

    @Getter
    private final String value;
}
