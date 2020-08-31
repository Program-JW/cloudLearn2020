package com.ruigu.rbox.workflow.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 闪电链值班策略VO
 *
 * @author liqingtian
 * @date 2020/05/07 15:28
 */
@Data
public class LightningDutyRuleVO {

    @ApiModelProperty(value = "策略id", name = "ruleId")
    private Integer ruleId;

    @ApiModelProperty(value = "策略名称", name = "ruleName")
    private String ruleName;

    @ApiModelProperty(value = "部门id", name = "departmentId")
    private Integer departmentId;

    @ApiModelProperty(value = "部门名称", name = "departmentName")
    private String departmentName;

    @ApiModelProperty(value = "状态 ( 0-禁用 1-启用 )", name = "status")
    private Integer status;

    @ApiModelProperty(value = "类型 ( 1-按天轮流分配 2-按问题轮流分配 )", name = "type")
    private Integer type;

    @ApiModelProperty(value = "使用范围标记 （1-产品技术专用）", name = "scopeType")
    private Integer scopeType;

    @ApiModelProperty(value = "是否预定义的 （0-否 1-是）", name = "preDefined")
    private Integer preDefined;
}
