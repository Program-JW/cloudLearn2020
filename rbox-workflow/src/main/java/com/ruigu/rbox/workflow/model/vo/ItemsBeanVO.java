package com.ruigu.rbox.workflow.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 采购单详情商品项
 * @author jianghuilin
 */
@NoArgsConstructor
@Data
public class ItemsBeanVO {
    /**
     * skuId : 21489
     * skuCode : 263690166
     * productName : 手电钻 TBM3400 340W 博世
     * minUnitQuantity : 300
     * minUnitPurchasingPrice : 107.000000
     * minUnitPromotionPrice : 107.000000
     * isTaxed : 1
     * quantity : 300
     * price : 107.000000
     * taxPrice : 15.5465
     * taxAmount : 4663.95
     * netPrice : 91.45
     * netAmount : 27435
     * amount : 32100.000000
     * taxRate : 0.17
     */

    private Integer skuId;
    private String skuCode;
    private String productName;
    private Integer minUnitQuantity;
    private String minUnitPurchasingPrice;
    private String minUnitPromotionPrice;
    private Integer isTaxed;
    private Integer quantity;
    private String price;
    private Double taxPrice;
    private Double taxAmount;
    private Double netPrice;
    private Integer netAmount;
    private String amount;
    private String taxRate;
}
