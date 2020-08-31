package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/8/17 13:37
 */
@Data
public class SpecialAfterSaleQuotaRuleEnableRequest {

    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "状态(0-禁用,1-启用)", name = "enable")
    private Integer enable;

    @NotNull(message = "额度规则编号不能为空")
    @ApiModelProperty(value = "额度规则编号", name = "ruleId")
    private Integer ruleId;

}
