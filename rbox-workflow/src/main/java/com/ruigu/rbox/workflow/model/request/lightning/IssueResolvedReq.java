package com.ruigu.rbox.workflow.model.request.lightning;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author caojinghong
 * @date 2020/01/03 11:24
 */
@Data
public class IssueResolvedReq {
    @ApiModelProperty(value = "问题id", name = "issueId", required = true)
    @NotNull(message = "问题id不能为空")
    private Integer issueId;
    @ApiModelProperty(value = "任务id", name = "taskId", required = true)
    @NotBlank(message = "任务id不能为空")
    private String taskId;
    @ApiModelProperty(value = "问题原因", name = "issueReason", required = true)
    @NotBlank(message = "问题原因不能为空")
    private String issueReason;
    @ApiModelProperty(value = "问题部门id", name = "issueDepartmentId", required = true)
    @NotNull(message = "问题部门id不能为空")
    private Integer issueDepartmentId;
    @ApiModelProperty(value = "是否转化为需求:单选 1-是/0-否", name = "demand", required = true)
    private Boolean demand;
}
