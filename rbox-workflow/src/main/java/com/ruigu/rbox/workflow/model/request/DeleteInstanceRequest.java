package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 删除实例请求
 *
 * @author alan.zhao
 */
@Data
@ApiModel(description = "删除流程")
public class DeleteInstanceRequest {
    @ApiModelProperty(name = "instanceId", value = "流程id", required = true)
    @NotBlank(message = "流程id不能为空")
    private String instanceId;

    @ApiModelProperty(name = "deleteReason", value = "删除原因", required = false)
    private String deleteReason;

    @ApiModelProperty(name = "deleteReason", value = "操作人id", required = false)
    @NotNull(message = "操作人id不能为空")
    private Integer userId;
}
