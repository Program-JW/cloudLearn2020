package com.ruigu.rbox.workflow.model.vo;

import lombok.Data;

/**
 * 邮件附件参数
 * @author alan.zhao
 */
@Data
public class EmailAttachment {
    private String name;
    private String url;

    public EmailAttachment() {
    }

    public EmailAttachment(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
