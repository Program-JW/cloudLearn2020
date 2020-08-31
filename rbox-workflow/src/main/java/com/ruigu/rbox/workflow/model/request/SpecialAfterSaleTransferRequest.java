package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/10 18:46
 */
@Data
public class SpecialAfterSaleTransferRequest {

    @ApiModelProperty(value = "申请id", name = "applyId")
    @NotNull(message = "申请id不能为空")
    private Long applyId;

    @ApiModelProperty(value = "任务id", name = "taskId")
    @NotBlank(message = "任务id不能为空")
    private String taskId;

    @ApiModelProperty(value = "转审人员", name = "userIds")
    @NotEmpty(message = "转审人不能为空")
    private List<Integer> userIds;

}
