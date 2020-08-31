package com.ruigu.rbox.workflow.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author caojinghong
 * @date 2019/12/27 18:31
 */
@ApiModel(value = "评价信息")
@Data
@Entity
@Table(name = "lightning_issue_evaluation")
public class LightningIssueEvaluationEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int")
    private Integer id;
    /**
     * 问题id
     */
    @Column(name = "issue_id", columnDefinition = "int")
    @ApiModelProperty(value = "问题id",name = "issueId")
    private Integer issueId;
    /**
     * 评价分数
     */
    @Column(name = "score", columnDefinition = "int")
    @ApiModelProperty(value = "评价分数",name = "score")
    private Integer score;
    /**
     * 最佳处理人id
     */
    @Column(name = "best_person_id", columnDefinition = "int")
    @ApiModelProperty(value = "最佳处理人id",name = "bestPersonId")
    private Integer bestPersonId;
    /**
     * 最佳处理人名称
     */
    @Column(name = "best_person_name", columnDefinition = "varchar")
    @ApiModelProperty(value = "最佳处理人名称",name = "bestPersonName")
    private String bestPersonName;
    /**
     * 创建人
     */
    @Column(name = "created_by", columnDefinition = "int")
    @ApiModelProperty(value = "评价人id",name = "createdBy")
    private Integer createdBy;
    /**
     * 创建时间
     */
    @Column(name = "created_at", columnDefinition = "timestamp")
    @ApiModelProperty(value = "评价时间",name = "createdAt")
    private Date createdAt;
    /**
     * 最后更新人
     */
    @Column(name = "last_updated_by", columnDefinition = "int")
    private Integer lastUpdatedBy;
    /**
     * 最后更新时间
     */
    @Column(name = "last_updated_at", columnDefinition = "timestamp")
    private Date lastUpdatedAt;

    /**
     * 流程id
     */
    @Column(name = "instance_id", columnDefinition = "varchar")
    private String instanceId;

}
