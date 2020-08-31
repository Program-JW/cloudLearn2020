package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author alan.zhao
 * @date 2020/01/17 01:38
 */
@AllArgsConstructor
@Getter
public enum LightningIssueRoleType {

    /**
     * 其他
     */
    OTHER(0, "其他"),

    /**
     * 发起人
     */
    INITIATOR(1, "发起人"),

    /**
     * 受理人
     */
    ASSIGNEE(2, "受理人");


    private Integer code;
    private String desc;

    public static String getDesc(int code) {
        for (LightningIssueRoleType status : LightningIssueRoleType.values()) {
            if (status.code == code) {
                return status.desc;
            }
        }
        return null;
    }
}
