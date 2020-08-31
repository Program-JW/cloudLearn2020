package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author liqingtian
 * @date 2019-12-26 16:29:31
 */
@Data
@Entity
@Table(name = "lightning_issue_apply")
@DynamicUpdate
public class LightningIssueApplyEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int")
    private Integer id;

    /**
     * 问题所属分类ID
     */
    @Column(name = "category_id", columnDefinition = "int")
    private Integer categoryId;

    /**
     * 问题描述
     */
    @Column(name = "description", columnDefinition = "varchar")
    private String description;

    /**
     * 期望处理人
     */
    @Column(name = "expected_solver", columnDefinition = "int")
    private Integer expectedSolver;

    /**
     * 当前受理人
     */
    @Column(name = "current_solver_id", columnDefinition = "int")
    private Integer currentSolverId;

    /**
     * 流程定义id
     */
    @Column(name = "definition_id", columnDefinition = "varchar")
    private String definitionId;

    /**
     * 流程id
     */
    @Column(name = "instance_id", columnDefinition = "varchar")
    private String instanceId;

    /**
     * 创建人ID
     */
    @Column(name = "created_by", columnDefinition = "int")
    private Integer createdBy;

    /**
     * 创建人名称(冗余字段)
     */
    @Column(name = "creator", columnDefinition = "varchar")
    private String creator;

    /**
     * 创建时间
     */
    @Column(name = "created_on", columnDefinition = "timestamp")
    private Date createdOn;

    /**
     * 最后修改者
     */
    @Column(name = "last_updated_by", columnDefinition = "int")
    private Integer lastUpdatedBy;

    /**
     * 最后修改日期
     */
    @Column(name = "last_updated_on", columnDefinition = "timestamp")
    private Date lastUpdatedOn;

    /**
     * 状态： -1 作废 0 发起 1 待受理 2 受理中 3 待确认 4 已解决 5 未解决
     */
    @Column(name = "status", columnDefinition = "int")
    private Integer status;
    /**
     * 附件清单，逗号分隔的文件列表
     */
    @Column(name = "attachments", columnDefinition = "text")
    private String attachments;

    /**
     * 问题原因
     */
    @Column(name = "issue_reason", columnDefinition = "varchar")
    private String issueReason;

    /**
     * 撤销原因
     */
    @Column(name = "revoke_reason", columnDefinition = "varchar")
    private String revokeReason;

    /**
     * 问题部门id
     */
    @Column(name = "issue_department_id", columnDefinition = "int")
    private Integer issueDepartmentId;

    /**
     * 是否转化为需求：0否 1是
     */
    @Column(name = "is_demand", columnDefinition = "tinyint")
    private Integer demand;
    /**
     * 未解决原因
     */
    @Column(name = "unresolved_reason", columnDefinition = "text")
    private String unresolvedReason;
    /**
     * 是否系统自动确认已解决
     */
    @Column(name = "is_auto_confirm", columnDefinition = "int")
    private Integer autoConfirm;
}
