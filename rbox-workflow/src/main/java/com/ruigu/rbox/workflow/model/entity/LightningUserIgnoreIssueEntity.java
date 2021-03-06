package com.ruigu.rbox.workflow.model.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author caojinghong
 * @date 2020/03/06 15:59
 */
@ApiModel(value = "闪电链用户忽略的问题表")
@Data
@Entity
@Table(name = "lightning_user_ignore_issue")
public class LightningUserIgnoreIssueEntity {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int")
    private Integer id;

    /**
     * 所属问题ID
     */
    @Column(name = "issue_id", columnDefinition = "int")
    private Integer issueId;
    /**
     * 人员id
     */
    @Column(name = "user_id", columnDefinition = "int")
    private Integer userId;

    /**
     * 创建人
     */
    @Column(name = "created_by", columnDefinition = "int")
    private Integer createdBy;
    /**
     * 创建时间
     */
    @Column(name = "created_on", columnDefinition = "timestamp")
    private Date createdOn;
    /**
     * 最后更新人
     */
    @Column(name = "last_updated_by", columnDefinition = "int")
    private Integer lastUpdatedBy;
    /**
     * 最后更新时间
     */
    @Column(name = "last_updated_on", columnDefinition = "timestamp")
    private Date lastUpdatedOn;
}
