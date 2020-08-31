package com.ruigu.rbox.workflow.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author liqingtian
 * @date 2019/10/16 17:28
 */
@Data
public class OperationLogVO {
    private Integer id;
    private String content;
    private Integer status;
    private String definitionId;
    private String definitionName;
    private String instanceId;
    private String instanceName;
    private String taskId;
    private String taskName;
    private Date createdOn;
    private Integer createdBy;
    private String creator;
    private Integer lastUpdatedBy;
    private Date lastUpdatedOn;

    public OperationLogVO() {
    }

    public OperationLogVO(Integer id, String content, Integer status, String definitionId, String definitionName, String instanceId, String taskId, String taskName, Date createdOn, Integer createdBy, Integer lastUpdatedBy, Date lastUpdatedOn) {
        this.id = id;
        this.content = content;
        this.status = status;
        this.definitionId = definitionId;
        this.definitionName = definitionName;
        this.instanceId = instanceId;
        this.taskId = taskId;
        this.taskName = taskName;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdatedOn = lastUpdatedOn;
    }
}
