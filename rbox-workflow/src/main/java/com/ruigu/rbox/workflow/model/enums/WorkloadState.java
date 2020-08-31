package com.ruigu.rbox.workflow.model.enums;

import lombok.Getter;

/**
 * @author liqingtian
 * @date 2019/10/12 17:36
 */
public enum WorkloadState {

    /**
     * 月统计
     */
    MONTH(1, "MONTH"),

    /**
     * 年统计
     */
    YEAR(2, "YEAR");

    @Getter
    private Integer code;

    @Getter
    private String value;

    WorkloadState(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
