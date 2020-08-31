package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author chenzhenya
 * @date 2019/11/13 16:44
 */

@Data
public class StockLockApplyEntity {

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
     * 临时字段,商品编码
     */
    private Integer skuCode;

    /**
     * 临时字段,数量
     */
    private Integer count;

    /**
     * 计划开始时间
     */
    private Date planLockTime;

    /**
     * 计划结束时间
     */
    private Date planUnlockTime;

    /**
     * 实际锁库存时间
     */
    private Date actualLockTime;

    /**
     * 实际释放库存时间
     */
    private Date actualUnlockTime;

    /**
     * 变更执行完成 0未完成 1已完成
     */
    private Integer locked;

    /**
     * 审批完成时间
     */
    private Date approvalTime;

    /**
     * 流程定义id
     */
    private String definitionId;

    /**
     * 流程id
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
}
