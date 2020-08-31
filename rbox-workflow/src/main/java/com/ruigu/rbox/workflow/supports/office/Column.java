package com.ruigu.rbox.workflow.supports.office;

import lombok.Data;

/**
 * Excel 列定义
 * @author alan.zhao
 */
@Data
public class Column {
    private String name;
    private String title;
    private Integer width;
    private String format;
    private boolean virtual;

    public Column() {
    }

    public Column(String name, String title) {
        this.name = name;
        this.title = title;
    }

    public Column(String name, String title, String format) {
        this.name = name;
        this.title = title;
        this.format = format;
    }

    public Column(String name, String title, Integer width) {
        this.name = name;
        this.title = title;
        this.width = width;
    }

    public Column(String name, String title, Integer width, String format) {
        this.name = name;
        this.title = title;
        this.width = width;
        this.format = format;
    }

    public Column(String name, String title, boolean virtual) {
        this.name = name;
        this.title = title;
        this.virtual = virtual;
    }
}
