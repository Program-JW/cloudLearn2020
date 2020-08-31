package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/05/06 17:47
 */
@Data
public class UpdateCategoryDutyUserRequest {

    @ApiModelProperty(value = "策略id", name = "ruleId")
    @NotNull(message = "策略id不能为空")
    private Integer ruleId;

    @ApiModelProperty(value = "值班人列表", name = "dutyUserList")
    @NotEmpty(message = "值班人列表不能为空")
    private List<Integer> dutyUserList;
}
