package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author liqingtian
 * @date 2020/05/07 18:06
 */
@Data
public class AddDutyRuleRequest {

    @ApiModelProperty(value = "策略名称", name = "ruleName", required = true)
    @NotBlank(message = "策略名称不能为空")
    private String ruleName;

    @ApiModelProperty(value = "绑定部门id", name = "departmentId", required = true)
    @NotNull(message = "部门id不能为空")
    private Integer departmentId;

    @ApiModelProperty(value = "类型 ( 1-按天轮流分配 2-按问题轮流分配 )", name = "type")
    @NotNull(message = "类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "值班人信息", name = "dutyUserRequest", required = true)
    private DutyUserRequest dutyUser;
}
