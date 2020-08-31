package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author chenzhenya
 * @date 2020-08-11 14:46:05
 */
@Data
@Entity
@Table(name = "special_after_sale_review")
public class SpecialAfterSaleReviewEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**
     * 主键
     */
    @Column(name = "id", columnDefinition = "int")
    private Integer id;

    /**
     * 抄送人ID集合,逗号分隔的抄送人ID列表
     */
    @Column(name = "cc_ids", columnDefinition = "varchar")
    private String ccIds;

    /**
     * 执行人ID集合,逗号分隔的执行人ID列表
     */
    @Column(name = "executor_ids", columnDefinition = "varchar")
    private String executorIds;

    /**
     * 审核配置名称
     */
    @Column(name = "name", columnDefinition = "varchar")
    private String name;

    /**
     * 审核配置描述
     */
    @Column(name = "description", columnDefinition = "varchar")
    private String description;

    /**
     * 组ID
     */
    @Column(name = "group_id", columnDefinition = "int")
    private Integer groupId;

    /**
     * 状态
     */
    @Column(name = "status", columnDefinition = "int")
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "created_at", columnDefinition = "datetime")
    private LocalDateTime createdAt;

    /**
     * 最后更新时间
     */
    @Column(name = "last_update_at", columnDefinition = "datetime")
    private LocalDateTime lastUpdateAt;

    /**
     * 创建人
     */
    @Column(name = "created_by", columnDefinition = "int")
    private Integer createdBy;

    /**
     * 最后更新人
     */
    @Column(name = "last_update_by", columnDefinition = "int")
    private Integer lastUpdateBy;

}
