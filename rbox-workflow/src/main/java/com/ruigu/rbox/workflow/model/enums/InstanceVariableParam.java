package com.ruigu.rbox.workflow.model.enums;

import lombok.Getter;

/**
 * @author liqingtian
 * @date 2019/12/24 17:24
 */
@Getter
public enum InstanceVariableParam {

    /**
     * 定义
     */
    DEFINITION_ID("definitionId"),
    DEFINITION_NAME("definitionName"),
    INITIAL_CODE("initialCode"),

    /**
     * 实例
     */
    INSTANCE_ID("instanceId"),
    INSTANCE_NAME("instanceName"),
    INSTANCE_CREATOR_ID("instanceCreatorId"),
    INSTANCE_CREATOR_NAME("instanceCreatorName"),
    INSTANCE_CREATE_TIME("instanceCreateTime"),
    INSTANCE_DETAIL_URL("url"),

    /**
     * 撤销原因
     */
    REVOCATION_REASON("revocationReason"),

    /**
     * 任务
     */
    TASK_ID("taskId"),
    TASK_NAME("taskName"),
    TASK_DETAIL_URL("url"),

    /**
     * 业务参数
     */
    BUSINESS_KEY("businessKey"),
    BUSINESS_PARAM("businessParams"),

    /**
     * =============================================
     * ----------------- 闪电链
     * =============================================
     */
    /**
     * 受理人
     */
    RECEIVER("receiver"),

    RECEIVER_NAME("receiverName"),

    /**
     * 当前受理人
     */
    PREVIOUS_RECEIVER("previousReceiver"),

    /**
     * 问题描述
     */
    DESCRIPTION("description"),

    /**
     * 原因总结
     */
    CAUSE_SUMMARY("causeSummary"),
    /**
     * 未解决原因
     */
    UNSOLVED_REASON("unsolvedReason"),

    /**
     * 闪电链 - leader名字0
     */
    LEADER_NAME("leaderName"),

    /**
     * 闪电链 - 未读消息内容
     */
    UNREAD_CHAT_MESSAGE("unreadChatMessage"),

    /**
     * 闪电链 - 未读消息发送时间
     */
    UNREAD_CHAT_SEND_TIME("unreadChatSendTime"),

    /**
     * 确认通知标识
     */
    CONFIRM_NOTICE_FLAGE("confirmNoticeFlag"),

    /**
     * 换行符
     */
    LINE_FEED("br");

    private String text;

    InstanceVariableParam(String text) {
        this.text = text;
    }
}
