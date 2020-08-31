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
public class StockBalancedSkuEntity implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 仓库ID
     */
    private Integer storageId;

    /**
     * 商品编码
     */
    @Column(name = "sku_code", columnDefinition = "int")
    private Integer skuCode;

    /**
     * 拉平时间
     */
    private Date actualTime;

    /**
     * 创建时间
     */
    private Date createdOn;

    /**
     * 最后修改日期
     */
    private Date lastUpdatedOn;

}
