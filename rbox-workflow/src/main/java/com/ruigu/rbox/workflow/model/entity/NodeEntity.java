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
@Table(name = "node")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class NodeEntity implements Serializable {
    @Id
    private String id;
    private String modelId;
    private String graphId;
    private String name;
    private String description;
    private Integer type;
    private Integer approvalNode;
    private String dueTime;
    private String candidateUsers;
    private String candidateGroups;
    @Lob
    private String formLocation;
    @Lob
    private String formContent;
    private Long createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    private Long lastUpdatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedOn;
    private Integer status;

    private Integer noticeType;
    private String noticeContent;

    private String businessUrl;

    private String remarks;

    @Lob
    private String summary;
}
