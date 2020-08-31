package com.ruigu.rbox.workflow.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/8/15 13:29
 */
@Data
public class SpecialAfterSaleQuotaRuleHistoryRequest extends PageableRequest {

    @NotNull(message = "审批规则编号不能为空")
    private Integer ruleId;
}
