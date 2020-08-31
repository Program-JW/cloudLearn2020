package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2019/12/27 10:18
 */
@AllArgsConstructor
@Getter
public enum LightningApplyStatus {

    /**
     * 状态： -1 作废 0 发起 1 待受理 2 受理中 3 待确认 4 已解决 5 未解决 6 已交接 7 已撤销
     */
    INVALID(-1, "作废"),

    START(0, "发起"),

    TO_BE_ACCEPTED(1, "待受理"),

    ACCEPTING(2, "受理中"),

    TO_BE_CONFIRMED(3, "待确认"),

    RESOLVED(4, "已解决"),

    UNRESOLVED(5, "未解决"),

    HANDED_OVER(6, "交接"),

    REVOKED(7, "已撤销"),

    URGE(8, "催办"),

    RESUBMIT(9, "重新提交"),

    ADD_MEMBER(10, "添加成员");

    private Integer code;
    private String desc;

    public static String getDesc(int code) {
        for (LightningApplyStatus status : LightningApplyStatus.values()) {
            if (status.code == code) {
                return status.desc;
            }
        }
        return null;
    }
}
