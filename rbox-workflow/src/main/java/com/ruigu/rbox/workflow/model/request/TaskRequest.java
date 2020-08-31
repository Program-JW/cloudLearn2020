package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author liqingtian
 * @date 2019/09/04 15:22
 */
@Data
public class TaskRequest {
    @ApiModelProperty(value = "任务ID", name = "taskId", required = true)
    @NotBlank(message = "任务ID不能为空")
    private String taskId;
}
