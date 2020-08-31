package com.ruigu.rbox.workflow.model.request;

import com.ruigu.rbox.workflow.model.dto.SpecialAfterSaleApproverDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author panjianwei
 * @date 2020/08/11 16:51
 */
@Data
public class SpecialAfterSaleApplyConfigRequest {

    @ApiModelProperty(value = "审批规则名称", name = "configName")
    private String configName;

    @NotNull(message = "申请人部门不能为空")
    @ApiModelProperty(value = "申请人部门", name = "department")
    private String department;

    @NotNull(message = "申请人职位列表不能为空")
    @ApiModelProperty(value = "申请人职位列表", name = "positions")
    private List<String> positions;

    @ApiModelProperty(value = "申请人组编号", name = "groupId")
    private Integer groupId;

    @ApiModelProperty(value = "描述", name = "description")
    private String description;

    @ApiModelProperty(value = "审批人列表", name = "approverList")
    private List<SpecialAfterSaleApproverDTO> approverList;

    @ApiModelProperty(value = "抄送人列表", name = "ccList")
    private List<String> ccList;

    @ApiModelProperty(value = "配置id", name = "configId", required = false)
    private Integer configId;


}
