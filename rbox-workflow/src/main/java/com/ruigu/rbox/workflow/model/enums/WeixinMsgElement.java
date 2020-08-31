package com.ruigu.rbox.workflow.model.enums;

import lombok.Getter;

/**
 * @author liqingtian
 * @date 2019/09/11 11:02
 */
public enum WeixinMsgElement {

    /**
     * 发送人
     */
    FROM_USER_NAME("fromUserName"),

    /**
     * 应用id
     */
    AGENT_ID("agentId"),

    /**
     * 消息类型
     */
    MSG_TYPE("msgType"),

    /**
     * 创建时间
     */
    CREATE_TIME("createTime"),

    /**
     * 按钮key
     */
    EVENT_KEY("eventKey"),

    /**
     * 接收人
     */
    TO_USER_NAME("toUserName"),

    /**
     * 事件类型
     */
    EVENT("event"),

    /**
     * 任务id
     */
    TASK_ID("taskId"),

    /**
     * msg type : enevt
     */
    MSG_TYPE_EVENT("event"),

    /**
     * msg type : text
     */
    MSG_TYPE_TEXT("text"),

    /**
     * taskId prefix
     */
    TASK_ID_PREFIX("TTB@");

    @Getter
    private final String desc;

    WeixinMsgElement(String desc) {
        this.desc = desc;
    }
}
