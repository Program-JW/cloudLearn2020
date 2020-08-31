package com.ruigu.rbox.workflow.model.enums;

import lombok.Getter;

/**
 * @author liqingtian
 * @date 2019/11/03 10:24
 */
public enum StockChangeApplyParam {

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
     * 单号
     */
    RECORD_CODE(5, "recordCode"),
    PHP_RECORD_CODE(6, "record_code"),

    /**
     * 推送人
     */
    PUBLISH_BY(7, "publishBy"),
    PHP_PUBLISH_BY(8, "publish_by"),

    /**
     * 变更详情
     */
    ITEM(9, "item"),

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
     * 是否增加
     */
    INCREASED(10, "increased"),

    /**
     * 是否立即执行
     */
    IMMEDIATELY(11, "immediately"),

    /**
     * 计划变更时间
     */
    PLAN_TIME(12, "planTime"),

    PHP_FROM_TTB(13, "is_ttb");

    @Getter
    private int code;

    @Getter
    private String desc;

    StockChangeApplyParam(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
