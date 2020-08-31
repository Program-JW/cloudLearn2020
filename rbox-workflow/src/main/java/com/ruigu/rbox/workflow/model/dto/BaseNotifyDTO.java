package com.ruigu.rbox.workflow.model.dto;

import lombok.Data;

/**
 * @author caojinghong
 * @date 2019/11/25 15:57
 */
@Data
public class BaseNotifyDTO {
    /**
     * 消息ID
     */
    private Long msgId;
    private String content;
    private Integer action;
}
