package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2019/11/11 15:45
 */
@Getter
@AllArgsConstructor
public enum TimeoutParam {

    /**
     * 闪电链
     */
    TIME_OUT_FOUR_HOURS(4, '0'),
    TIME_OUT_TWENTY_FOUR_HOURS(24, '0'),
    TIME_OUT_FORTY_EIGHT_HOURS(48, '0'),

    /**
     * 周
     */
    W(1, 'w'),

    /**
     * x天内
     */
    I(2, 'i'),

    /**
     * 天
     */
    D(3, 'd'),

    /**
     * 小时
     */
    H(4, 'h'),

    /**
     * 分钟
     */
    M(5, 'm');

    private int code;

    private char text;
}
