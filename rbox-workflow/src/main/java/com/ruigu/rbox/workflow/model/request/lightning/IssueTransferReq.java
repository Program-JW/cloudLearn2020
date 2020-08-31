package com.ruigu.rbox.workflow.model.request.lightning;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author caojinghong
 * @date 2020/01/03 10:29
 */
@Data
public class IssueTransferReq {
    @ApiModelProperty(value = "问题id", name = "issueId", required = true)
    @NotNull(message = "问题id不能为空")
    private Integer issueId;
    @ApiModelProperty(value = "任务id", name = "taskId", required = true)
    @NotBlank(message = "任务id不能为空")
    private String taskId;
    @ApiModelProperty(value = "交接人id", name = "assigneeId", required = true)
    @NotNull(message = "交接人id不能为空")
    private Integer assigneeId;

}
