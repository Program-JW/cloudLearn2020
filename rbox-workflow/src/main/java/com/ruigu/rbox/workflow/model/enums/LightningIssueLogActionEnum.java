package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author caojinghong
 * @date 2019/12/31 18:00
 */
@AllArgsConstructor
public enum LightningIssueLogActionEnum {
    /**
     * 状态：  0 发起 1 已受理 2 已交接 3 提交确认 4 确认已解决 5 确认未解决 6 已撤销 7-超时4小时  8-超时24小时 9-超时48小时 11-自动确认 12-自动提交
     */
    START(0, "发起"),

    ACCEPTED(1, "已受理"),

    HANDED_OVER(2, "已交接"),

    SUBMIT_CONFIRMED(3, "提交确认"),

    RESOLVED(4, "确认已解决"),

    UNRESOLVED(5, "确认未解决"),

    REVOKED(6, "已撤销"),

    TIME_OUT_4(7, "超时4小时"),

    TIME_OUT_24(8, "超时24小时"),

    TIME_OUT_48(9, "超时48小时"),

    TO_BE_ACCEPTED(10, "待受理"),

    AUTO_CONFIRM(11, "系统自动确认"),

    RESUBMIT(12, "重新提交"),

    /**
     * 不对外展示
     */
    LEADER_ADD(13, "领导拉人"),
    AUTO_HANDED_OVER(14, "系统自动交接"),
    INVITE_HANDED_OVER(15, "邀请交接"),
    AUTO_CLOSE(16, "自动关闭");

    @Getter
    @Setter
    private Integer code;
    @Getter
    @Setter
    private String desc;
}
