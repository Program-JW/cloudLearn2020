package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author liqingtian
 * @date 2020/05/08 13:53
 */
@AllArgsConstructor
@NoArgsConstructor
public enum DutyRuleTypeEnum {
    /**
     * 策略类型
     */
    DUTY_BY_DAY(1, "按天值班"),
    DUTY_POLL(2, "轮询值班"),
    DUTY_BY_WEEK(3, "按周排班");

    @Getter
    private Integer code;

    @Getter
    private String value;
}
