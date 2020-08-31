package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/10 18:46
 */
@Data
public class SpecialAfterSaleDxTransferRequest {

    @ApiModelProperty(value = "申请id", name = "applyId")
    @NotNull(message = "申请id不能为空")
    private Long applyId;

    @ApiModelProperty(value = "审批配置节点id", name = "nodeId")
    @NotNull(message = "审批配置节点id不能为空")
    private Integer nodeId;

    @ApiModelProperty(value = "转审人员", name = "userIds")
    @NotEmpty(message = "转审人不能为空")
    private List<Integer> userIds;

}
