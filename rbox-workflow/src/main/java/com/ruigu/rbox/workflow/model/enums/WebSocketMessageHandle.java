package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2020/01/09 10:20
 */
@Getter
@AllArgsConstructor
public enum WebSocketMessageHandle {

    /**
     * 添加
     */
    ADD_USER_TO_GROUP(22, "addUserToGroupHandleStrategy"),

    /**
     * 建群
     */
    ROBOT_BUILD_GROUP(16, "buildGroupHandleStrategy");

    private int code;
    private String value;

    public static String getValue(Integer code) {
        for (WebSocketMessageHandle handle : WebSocketMessageHandle.values()) {
            if (handle.code == code) {
                return handle.value;
            }
        }
        return null;
    }
}
