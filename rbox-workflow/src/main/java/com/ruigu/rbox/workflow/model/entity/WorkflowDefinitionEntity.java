package com.ruigu.rbox.workflow.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author alan.zhao
 */
@Entity
@Table(name = "workflow_definition")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class WorkflowDefinitionEntity implements Serializable {
    @Id
    private String id;
    private String name;
    private String description;
    private String initialCode;
    private BigInteger version;
    private Integer isReleased;
    private String deploymentId;
    private Long createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    private Long lastUpdatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedOn;
    private Integer status;
    private String businessUrl;
    private String pcBusinessUrl;
    private String mobileBusinessUrl;
    private String initialUrl;
    private String pcInitialUrl;
    private String mobileInitialUrl;
    private String outline;

    public WorkflowDefinitionEntity() {
    }

    public WorkflowDefinitionEntity(String id, String name, BigInteger version) {
        this.id = id;
        this.name = name;
        this.version = version;
    }
}
