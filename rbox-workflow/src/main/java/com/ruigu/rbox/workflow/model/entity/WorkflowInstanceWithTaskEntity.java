package com.ruigu.rbox.workflow.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author liqingtian
 * @date 2020/01/13 15:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowInstanceWithTaskEntity {

    /**
     * 实例信息
     */
    @Id
    private String id;
    private String name;
    private String businessKey;
    private String businessUrl;
    private String pcBusinessUrl;
    private String mobileBusinessUrl;
    private String definitionId;
    private String definitionCode;
    private Integer definitionVersion;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    private String deleteReason;
    private Long ownerId;
    private String sourcePlatform;
    private String sourcePlatformUserId;
    private String sourcePlatformUserName;
    private Long createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    private Long lastUpdatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedOn;
    private Integer status;

    /**
     * 任务信息
     */
    private String taskId;
    private String nodeId;
    private String modelId;
    private String graphId;
    private String taskName;
    private String taskDescription;
    private Integer taskType;
    private Integer approvalNode;
    private String taskDueTime;
    private String candidateUsers;
    private String candidateGroups;
    private Long taskSubmitBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date taskSubmitTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date taskBeginTime;
    private Long taskCreatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date taskCreatedOn;
    private Long taskLastUpdatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date taskLastUpdatedOn;
    private Integer taskStatus;
    private String remarks;
}
