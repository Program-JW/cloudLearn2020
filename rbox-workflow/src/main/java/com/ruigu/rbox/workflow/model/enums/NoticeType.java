package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2019/09/09 13:34
 */
@Getter
@AllArgsConstructor
public enum NoticeType {

    /**
     * 通道 - 微信
     */
    WEIXIN_CHANNEL(1, "weixin"),

    /**
     * 通道 - 邮箱
     */
    EMAIL_CHANNEL(2, "email"),

    /**
     * 微信 - 卡片通知
     */
    TASK_CARD(10, "taskCard"),

    /**
     * 微信 - 卡片通知
     */
    TEXT_CARD(11, "textCard"),

    /**
     * 微信 - 文本通知
     */
    TEXT(12, "text");

    private int state;

    private String desc;

    public static String getDesc(int state) {
        for (NoticeType type : NoticeType.values()) {
            if (type.getState() == state) {
                return type.getDesc();
            }
        }
        return null;
    }
}
