package com.ruigu.rbox.workflow.model;

import lombok.Data;

import java.util.List;

/**
 * @author liqingtian
 * @date 2019/09/17 14:53
 */
@Data
public class UpdateStatusMsg {

    private List<String> userIds;
    private Integer agentId;
    private String taskId;
    private String clickedKey;

}
