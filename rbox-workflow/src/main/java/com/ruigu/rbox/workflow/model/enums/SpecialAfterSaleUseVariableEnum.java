package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2020/08/12 14:05
 */
@AllArgsConstructor
public enum SpecialAfterSaleUseVariableEnum {

    APPLY_ID("applyId", "申请ID"),

    APPLY_USER_ID("applyUserId", "申请人ID"),

    APPLY_USER_TYPE("applyUserType", "申请人类型"),

    APPLY_TIME("applyTime", "申请时间"),

    APPLY_TOTAL_AMOUNT("applyTotalAmount", "申请总金额"),

    APPLY_REASON("applyReason", "申请原因"),

    CONFIG_ID("configId", "审批配置规则ID"),

    CURRENT_NODE("currentNode", "当前进行节点"),

    CURRENT_NODE_ID("currentNodeId", "当前进行节点ID"),

    CURRENT_NODE_SPECIFY_USER("currentNodeSpecifyUser", "当前进行节点指定人"),

    CURRENT_NODE_USER("currentNodeUser", "当前进行节点审批人"),

    HAVE_NEXT_NODE("haveNextNode", "是否有下一个审批节点"),

    IS_NEED_QUOTA("isNeedQuota", "是否需要额度"),

    Last_APPROVER("lastApprover", "最后审批人"),

    IS_ENOUGH_QUOTA("isEnoughQuota", "额度是否充足"),

    USE_QUOTA_ID("useQuotaId", "使用的额度ID"),

    APPROVAL_STATUS_DESC("approvalStatusDesc","审批状态描述"),

    ;

    @Getter
    private final String code;
    @Getter
    private final String desc;

}
