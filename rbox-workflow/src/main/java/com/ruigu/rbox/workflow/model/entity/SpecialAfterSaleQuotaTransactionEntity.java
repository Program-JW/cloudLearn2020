package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author chenzhenya
 * @date 2020-08-12 20:21:22
 */
@Data
@Entity
@Table(name = "special_after_sale_quota_transaction")
public class SpecialAfterSaleQuotaTransactionEntity implements Serializable {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint")
    private Long id;

    /**
     * 年月
     */
    @Column(name = "y_m", columnDefinition = "int")
    private Integer yM;

    /**
     * 申请id
     */
    @Column(name = "apply_id", columnDefinition = "bigint")
    private Long applyId;

    /**
     * 申请id
     */
    @Column(name = "quota_id", columnDefinition = "int")
    private Integer quotaId;

    /**
     * 组类型 1 城市组 2 BDM组
     */
    @Column(name = "group_type", columnDefinition = "int")
    private Integer groupType;

    /**
     * 组id
     */
    @Column(name = "group_id", columnDefinition = "int")
    private Integer groupId;

    /**
     * 扣减额度
     */
    @Column(name = "amount", columnDefinition = "decimal")
    private BigDecimal amount;

    /**
     * 类型 1 电销 2 直销
     */
    @Column(name = "type", columnDefinition = "int")
    private Integer type;

    /**
     * 状态 0-待扣 1-生效 2-失效
     */
    @Column(name = "status", columnDefinition = "int")
    private Integer status;

    /**
     * 创建人
     */
    @Column(name = "created_by", columnDefinition = "int")
    private Integer createdBy;

    /**
     * 创建时间
     */
    @Column(name = "created_at", columnDefinition = "datetime")
    private LocalDateTime createdAt;

    /**
     * 最后修改人
     */
    @Column(name = "last_updated_by", columnDefinition = "int")
    private Integer lastUpdatedBy;

    /**
     * 最后修改时间
     */
    @Column(name = "last_updated_at", columnDefinition = "datetime")
    private LocalDateTime lastUpdatedAt;

}
