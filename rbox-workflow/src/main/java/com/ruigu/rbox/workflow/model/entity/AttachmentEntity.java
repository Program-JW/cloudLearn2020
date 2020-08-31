package com.ruigu.rbox.workflow.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author alan.zhao
 */
@Entity
@Table(name = "attachment")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AttachmentEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String instanceId;

    private String taskId;

    private String name;

    private String ext;

    private String path;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    private Integer createdBy;

    private Integer status;
}
