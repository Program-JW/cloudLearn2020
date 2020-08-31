package com.ruigu.rbox.workflow.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
/**
 * 流程分类
 *
 * @author     ：jianghuilin
 * @date       ：Created in {2019/8/28} {14:38}
 */
@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "workflow_category")
public class WorkflowCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String definitionKey;
    private Integer parentId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    private Long createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedOn;
    private Long lastUpdatedBy;
    private byte status;
}
