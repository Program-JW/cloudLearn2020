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
@Table(name = "work_order")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class WorkOrderEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Integer type;
    private String title;
    @Lob
    private String detail;
    private Long customId;
    private String customName;
    private String customPhone;
    private String skuCode;
    private String orderCode;
    private Integer stage;
    private Integer isSolved;
    private Long createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    private Long lastUpdatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedOn;
    private Integer status;
}
