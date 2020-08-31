package com.ruigu.rbox.workflow.model.enums;

import lombok.Getter;

/**
 * @author chenzhenya
 * @date 2019/11/15 19:31
 */
public enum StockLockApplyParam {
    /**
     * 仓库ID
     */
    STORAGE_ID(1, "storageId"),
    PHP_STORAGE_ID(2, "storage_id"),

    /**
     * 库存类型
     */
    STOCK_TYPE(3, "stockType"),
    PHP_STOCK_TYPE(4, "stock_type"),

    /**
     * sku code
     */
    SKU_CODE(90, "skuCode"),
    PHP_SKU_CODE(91, "sku_code"),

    /**
     * 数量
     */
    COUNT(92, "count"),

    /**
     * 计划开始时间
     */
    BEGIN_TIME(12, "beginTime"),
    PHP_BEGIN_TIME(13, "begin_time"),

    /**
     * 计划结束时间
     */
    END_TIME(14, "endTime"),
    PHP_END_TIME(15, "end_time");

    @Getter
    private int code;

    @Getter
    private String desc;

    StockLockApplyParam(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
