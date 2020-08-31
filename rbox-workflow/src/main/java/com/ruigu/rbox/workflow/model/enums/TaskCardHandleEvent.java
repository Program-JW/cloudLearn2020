package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2019/12/31 16:40
 */
@Getter
@AllArgsConstructor
public enum TaskCardHandleEvent {

    /**
     * 提交任务
     */
    SAVE_TASK(1, "saveTaskHandleStrategy"),

    /**
     * 确认
     */
    SEND_CONFIRM_SIGNAL(2, "sendConfirmSignalHandleStrategy"),

    /**
     * 催办
     */
    URGE(3, "sendUrgeNoticeHandleStrategy"),

    /**
     * 催办未受理
     */
    URGE_BEGIN(4, "sendUrgeNoticeHandleStrategy");

    private Integer code;
    private String value;

    public static String getValue(Integer code) {
        for (TaskCardHandleEvent event : TaskCardHandleEvent.values()) {
            if (event.code.equals(code)) {
                return event.value;
            }
        }
        return null;
    }
}
