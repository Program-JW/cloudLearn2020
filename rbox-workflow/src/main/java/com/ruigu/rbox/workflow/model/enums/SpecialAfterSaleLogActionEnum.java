package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2020/08/11 18:00
 */
@AllArgsConstructor
public enum SpecialAfterSaleLogActionEnum {

    /**
     *
     */

    START(0, "start", "发起"),

    DX_MANAGER_TRANSFER(1, "dxManagerTransfer", "电销主管待转审"),

    DX_MANAGER_TRANSFERRED(2, "dxManagerTransferred", "电销主管已转审"),

    PENDING_APPROVAL(3, "pendingApproval", "待审批"),

    TRANSFER(4, "transfer", "转审"),

    PASS(5, "pass", "审批通过"),

    REJECT(6, "reject", "审批驳回"),

    END(7, "end", "结束"),

    CANCEL(8, "cancel", "撤销"),

    ;


    @Getter
    private final int code;
    @Getter
    private final String value;
    @Getter
    private final String desc;
}
