package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author caojinghong
 * @date 2020/02/17 18:20
 */
@AllArgsConstructor
@NoArgsConstructor
public enum QueryFormType {
    /**
     * 查询类型
     */
    LAST_MONTH(0, "上个月"),
    CURRENT_MONTH(1, "本月"),
    LAST_DAY(2, "昨日"),
    CUSTOM_TIME(3, "用户自定义时间");


    @Getter
    @Setter
    private Integer code;
    @Setter
    @Getter
    private String text;
}
