package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author chenzhenya
 * @date 2020-08-10 15:38:17
 */
@Data
@Entity
@Table(name = "special_after_sale_log")
public class SpecialAfterSaleLogEntity implements Serializable {

    /**
     * 主键
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint")
    private Long id;

    /**
     * 申请ID
     */
    @Column(name = "apply_id", columnDefinition = "bigint")
    private Long applyId;

    /**
     * 配置节点id
     */
    @Column(name = "review_node_id", columnDefinition = "int")
    private Integer reviewNodeId;

    /**
     * 任务id
     */
    @Column(name = "task_id", columnDefinition = "varchar")
    private String taskId;

    /**
     * 操作类型 待定
     */
    @Column(name = "action", columnDefinition = "int")
    private Integer action;

    /**
     * 备注
     */
    @Column(name = "remarks", columnDefinition = "text")
    private String remarks;

    /**
     * 创建人ID
     */
    @Column(name = "created_by", columnDefinition = "int")
    private Integer createdBy;

    /**
     * 是否显示
     */
    @Column(name = "is_show", columnDefinition = "int")
    private Integer show;

    /**
     * 创建时间
     */
    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    /**
     * 状态： -1 作废 1 启用 0 禁用
     */
    @Column(name = "status", columnDefinition = "int")
    private Integer status;

    /**
     * 最后修改者
     */
    @Column(name = "last_updated_by", columnDefinition = "int")
    private Integer lastUpdatedBy;

    /**
     * 最后更新时间
     */
    @Column(name = "last_updated_at", columnDefinition = "timestamp")
    private LocalDateTime lastUpdatedAt;

}
