package com.ruigu.rbox.workflow.model.enums;

import lombok.Getter;

/**
 * @author liqingtian
 * @date 2019/10/14 11:16
 */
public enum NoticeParam {
    /**
     * 标题
     */
    TITLE(1, "title"),

    /**
     * 描述
     */
    DESCRIPTION(2, "description"),

    /**
     * 任务id
     */
    TASK_ID(3, "taskId"),

    /**
     * 任务卡片btn
     */
    BTN(4, "btn"),

    /**
     * 任务卡片btn
     */
    BTN_KEY(41, "key"),

    /**
     * 任务卡片btn
     */
    BTN_NAME(42, "name"),

    /**
     * 任务卡片btn
     */
    BTN_REPLACE_NAME(43, "replaceName"),

    /**
     * 文本卡片btntxt
     */
    BTN_TXT(5, "btntxt"),

    /**
     * 文本卡片btntxt
     */
    URL(6, "url"),

    /**
     * email发送中的内容
     */
    CONTENT(7, "content"),

    /**
     * 通知类型
     */
    NOTICE_TYPE(8, "noticeType"),

    TARGET(10, "target"),

    LEADER(11, "leader");


    @Getter
    private int state;

    @Getter
    private String desc;

    NoticeParam(int state, String desc) {
        this.state = state;
        this.desc = desc;
    }
}
