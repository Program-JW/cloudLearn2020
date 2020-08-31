package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2019/09/09 16:32
 */
@Getter
@AllArgsConstructor
public enum DataType {

    /**
     * 其他 (统一字符串处理)
     */
    OTHER_DATA(0),

    /**
     * String
     */
    STRING_DATA(1),

    /**
     * Integer
     */
    INTEGER_DATA(2),

    /**
     * Double
     */
    DOUBLE_DATA(3),

    /**
     * Date
     */
    DATE_DATA(4);

    public int state;
}
