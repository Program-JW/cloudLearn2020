package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2020/04/28 15:36
 */
@AllArgsConstructor
public enum ChatContentTypeEnum {

    /**
     * 文本
     */
    TEXT(0, ""),

    /**
     * 图片
     */
    IMAGE(1, "[图片]");

    @Getter
    private Integer code;

    @Getter
    private String value;

    public static String queryValue(Integer code) {
        if (code == null) {
            return TEXT.getValue();
        }
        for (ChatContentTypeEnum e : ChatContentTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getValue();
            }
        }
        return TEXT.getValue();
    }
}
