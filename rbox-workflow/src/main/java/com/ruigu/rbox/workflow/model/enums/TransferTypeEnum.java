package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author caojinghong
 * @date 2020/04/28 16:04
 */
@AllArgsConstructor
public enum TransferTypeEnum {
    /**
     * 当前受理人交接
     */
    SOLVER_TRANSFER(0, "当前受理人交接"),
    /**
     * 离职系统交接
     */
    LEAVE_TRANSFER(1, "离职系统交接"),
    /**
     * 领导邀请人交接
     */
    INVITE_TRANSFER(2, "领导邀请人交接")
    ;
    @Getter
    @Setter
    private Integer code;
    @Getter
    @Setter
    private String desc;
}
