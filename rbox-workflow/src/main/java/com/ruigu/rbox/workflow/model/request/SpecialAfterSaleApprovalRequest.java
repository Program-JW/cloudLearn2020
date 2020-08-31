package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author liqingtian
 * @date 2020/08/10 18:40
 */
@Data
public class SpecialAfterSaleApprovalRequest {
    @ApiModelProperty(value = "申请id", name = "applyId")
    @NotNull(message = "申请id不能为空")
    private Long applyId;
    @ApiModelProperty(value = "任务id", name = "taskId")
    @NotBlank(message = "任务id不能为空")
    private String taskId;
    @ApiModelProperty(value = "状态", name = "status")
    @NotNull(message = "状态不能为空")
    private Integer status;
    @ApiModelProperty(value = "审批意见", name = "opinions")
    private String opinions;
    @ApiModelProperty(value = "额度id (当该节点需要额度时，需要传选择的额度id)", name = "quotaId")
    private Integer quotaId;
}
