package com.ruigu.rbox.workflow.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 流程表单
 * @author alan.zhao
 */
@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "workflow_form")
public class WorkflowFormEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String definitionId;
    private String formContent;
    private String selectFormContent;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    private Long createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedOn;
    private Long lastUpdatedBy;
    private Integer status;
}
