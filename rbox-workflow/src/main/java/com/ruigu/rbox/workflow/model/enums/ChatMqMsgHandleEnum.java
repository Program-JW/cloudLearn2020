package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2020/02/04 9:54
 */
@AllArgsConstructor
public enum ChatMqMsgHandleEnum {

    /**
     * 未读消息通知
     */
    UNREAD_MSG_HANDLE(1, "unreadMsgHandleStrategy");

    @Getter
    private Integer code;
    @Getter
    private String value;

    public static String getHandle(Integer code) {
        for (ChatMqMsgHandleEnum data : ChatMqMsgHandleEnum.values()) {
            if (data.code.equals(code)) {
                return data.value;
            }
        }
        return null;
    }
}
