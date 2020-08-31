package com.ruigu.rbox.workflow.model.enums;

import lombok.Getter;

/**
 * @author panjianwei
 * @date 2020/08/12 13:53
 */
public enum SpecialAfterSaleQuotaRuleTypeEnum {

    /**
     * 额度规则
     */
    QUOTA_RULE_TYPE(1, "额度规则"),
    /**
     * 额度规则历史
     */
    HISTORY_QUOTA_RULE_TYPE(2, "额度规则历史");

    @Getter
    private final int state;

    @Getter
    private final String mean;

    SpecialAfterSaleQuotaRuleTypeEnum(int state, String mean) {
        this.state = state;
        this.mean = mean;
    }
}
