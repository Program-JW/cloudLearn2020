package com.ruigu.rbox.workflow.model.dto;

import lombok.Data;

/**
 * @author liqingtian
 * @date 2019/12/31 17:22
 */
@Data
public class TaskCardReturnMessageDTO {
    private String fromUserName;
    private String agentId;
    private String msgType;
    private String createTime;
    private String eventKey;
    private String toUserName;
    private String event;
    private String taskId;
}
