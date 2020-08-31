package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2020/04/03 11:17
 */
@AllArgsConstructor
public enum RunningStatusEnum {

    /**
     * 是否正在运行
     */
    ALL(0, "全部"),
    RUNNING(1, "运行中"),
    STOP(2, "关闭");

    @Getter
    private Integer code;

    @Getter
    private String value;

}
