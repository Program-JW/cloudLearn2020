package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author liqingtian
 * @date 2019-11-02 21:35:18
 */
@Data
public class StockChangeDetailEntity implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 申请单ID
     */
    private Integer applyId;

    /**
     * sku编码
     */
    private Integer skuCode;

    /**
     * 数量
     */
    private Integer count;

}
