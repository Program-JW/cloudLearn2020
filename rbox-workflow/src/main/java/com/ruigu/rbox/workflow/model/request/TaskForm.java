package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author alan.zhao
 */
@Data
public class TaskForm {
    @ApiModelProperty(value = "任务id", name = "id")
    private String id;
    @ApiModelProperty(value = "任务提交参数", name = "formData")
    private List<TaskFormItem> formData;
    /**
     * 批处理 任务id列表
     */
    @ApiModelProperty(value = "批处理 任务id列表", name = "ids")
    private List<String> ids;
    /**
     * 批处理 审批参数
     */
    @ApiModelProperty(value = "批处理 审批参数", name = "approvalValue")
    private String approvalValue;
}
