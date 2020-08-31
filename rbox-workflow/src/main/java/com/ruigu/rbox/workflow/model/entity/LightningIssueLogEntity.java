package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author caojinghong
 * @date 2019/12/31 16:15
 */
@Data
@Entity
@Table(name = "lightning_issue_log")
public class LightningIssueLogEntity {
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
     * 操作类型 0 发起 1 已受理 2 已交接 3 提交确认 4 确认已解决 5 确认未解决 6 撤销 7 超时4小时 8 超时24小时 9 超时48小时 10 待受理
     */
    @Column(name = "action", columnDefinition = "int")
    private Integer action;
    /**
     * 创建人ID
     */
    @Column(name = "created_by", columnDefinition = "int")
    private Integer createdBy;

    /**
     * 创建时间
     */
    @Column(name = "created_on", columnDefinition = "timestamp")
    private Date createdOn;

    /**
     * 状态： -1 作废 1 启用 0 禁用
     */
    @Column(name = "status", columnDefinition = "int")
    private Integer status;

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
    /**
     * 备注（可以是各种操作的原因，如未解决原因、撤销原因、已解决原因等）
     */
    @Column(name = "remarks", columnDefinition = "text")
    private String remarks;

}
