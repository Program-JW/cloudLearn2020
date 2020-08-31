package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/12/25 16:12
 */
@Data
public class ConfirmSolveSignalRequest {
    @ApiModelProperty(value = "实例ID", name = "instanceId", required = true)
    @NotBlank(message = "实例ID不能为空。")
    private String instanceId;
    @ApiModelProperty(value = "确认操作参数", name = "variables")
    private Map<String, Object> variables;
}
