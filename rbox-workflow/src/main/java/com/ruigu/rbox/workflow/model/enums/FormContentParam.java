package com.ruigu.rbox.workflow.model.enums;

import lombok.Getter;

/**
 * @author liqingtian
 * @date 2019/11/11 15:22
 */
public enum FormContentParam {

    /**
     * 表单类型
     */
    TYPE(1, "type"),

    /**
     * 容器
     */
    CONTAINER(2, "container"),

    /**
     * 内容列表
     */
    LIST(3, "list"),

    /**
     * 选项
     */
    OPTIONS(4, "options"),

    /**
     *
     */
    CODE(5, "code"),

    /**
     *
     */
    DATASOURCE(6, "datasource"),

    /**
     * 值
     */
    VALUE(7, "value"),

    LABEL(8, "label"),

    KEY(9, "key");

    @Getter
    private int code;

    @Getter
    private String text;

    FormContentParam(int code, String text) {
        this.code = code;
        this.text = text;
    }
}
