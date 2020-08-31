package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author alan.zhao
 */
@Data
public class TaskTransferRequest {
    @ApiModelProperty(value = "任务ID", name = "id")
    private String id;
    @ApiModelProperty(value = "要转办的目标用户", name = "userId")
    private List<Integer> userId;
}
