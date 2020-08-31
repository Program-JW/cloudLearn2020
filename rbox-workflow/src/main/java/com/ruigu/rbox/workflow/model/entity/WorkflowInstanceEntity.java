package com.ruigu.rbox.workflow.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author alan.zhao
 */
@Entity
@Table(name = "workflow_instance")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class WorkflowInstanceEntity implements Serializable {
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
}
