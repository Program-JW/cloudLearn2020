package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author liqingtian
 * @date 2020/05/08 20:00
 */

@Data
@NoArgsConstructor
public class UpdateDutyRuleRequest {

    @ApiModelProperty(value = "策略ID", name = "ruleId")
    @NotNull(message = "策略ID不能为空")
    private Integer ruleId;

    @ApiModelProperty(value = "策略名称", name = "ruleName")
    @NotBlank(message = "策略名称不能为空")
    private String ruleName;

    @ApiModelProperty(value = "绑定部门id", name = "departmentId")
    @NotNull(message = "部门id不能为空")
    private Integer departmentId;

    @ApiModelProperty(value = "类型 ( 1-按天轮流分配 2-按问题轮流分配 )", name = "type")
    @NotNull(message = "类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "值班信息", name = "dutyUserRequest")
    private DutyUserRequest dutyUser;
}
