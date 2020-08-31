package com.ruigu.rbox.workflow.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author panjianwei
 * @date 2020/08/11 17:53
 */
@Data
public class SpecialAfterSaleApproverDTO {

    @NotNull(message = "审批人类型不能为空")
    @ApiModelProperty(value = "审批人类型标志(1-上级按职位,2-单人)", name = "flag", required = true)
    private Integer flag;

    @ApiModelProperty(value = "审批人编号(审批人为单人时填写)", name = "id", required = false)
    private Integer id;

    @ApiModelProperty(value = "职位id(审批人为职位时填写)", name = "positions", required = false)
    private List<String> positions;

    @ApiModelProperty(value = "节点名称", name = "name", required = false)
    private String name;

    @ApiModelProperty(value = "审核顺序", name = "sort", required = false)
    private Integer sort;

    @ApiModelProperty(value = "是否扣额度(1-是,2-否)", name = "useQuota", required = false)
    private Integer useQuota;


}
