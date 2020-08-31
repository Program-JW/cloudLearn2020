package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author liqingtian
 * @date 2019-11-02 21:35:17
 */
@Data
public class StockChangeApplyEntity implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 仓库ID
     */
    private Integer storageId;

    /**
     * 库存类型:1=自营总仓,2=省代总仓,3=自营预售,4=省代直发
     */
    private Integer stockType;

    /**
     * 是否是增加  0减少  1增加
     */
    private Integer increased;

    /**
     * 是否立即执行 0否 1是
     */
    private Integer immediately;

    /**
     * 计划开始时间
     */
    private Date planTime;

    /**
     * 最终执行完成时间
     */
    private Date actualTime;

    /**
     * 变更执行完成 0未完成 1已完成
     */
    private Integer completed;

    /**
     * 审批完成时间
     */
    private Date approvalTime;

    /**
     * 流程定义ID
     */
    private String definitionId;

    /**
     * 流程ID
     */
    private String instanceId;

    /**
     * 创建人ID
     */
    private Integer createdBy;

    /**
     * 创建人名称(冗余字段)
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createdOn;

    /**
     * 最后修改者
     */
    private Integer lastUpdatedBy;

    /**
     * 最后修改日期
     */
    private Date lastUpdatedOn;

    /**
     * 状态： -1 作废 0 待审批 1 审批中 3 已通过 4 已驳回
     */
    private Integer status;

    /**
     * 商品编码
     */
    @Transient
    private Integer skuCode;

    /**
     * 商品数量
     */
    @Transient
    private Integer count;

    /**
     * 是否拉平
     */
    @Transient
    private Integer unbalanced;
}
