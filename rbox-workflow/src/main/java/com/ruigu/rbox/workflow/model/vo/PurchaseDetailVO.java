package com.ruigu.rbox.workflow.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 采购单详情数据
 * @author jianghuilin
 */
@NoArgsConstructor
@Data
public class PurchaseDetailVO {

    /**
     * orderNumber : SPO2017110700056
     * orderType : 货物清单
     * orderTag : 订单标记
     * supplierName : 上海才有五金机电商行
     * paymentMethod : 自定义
     * totalAmount : 145710.000000
     * warehouseName : 虚拟地推仓
     * status : 7
     * items : [{"skuId":21489,"skuCode":"263690166","productName":"手电钻 TBM3400 340W 博世","minUnitQuantity":300,"minUnitPurchasingPrice":"107.000000","minUnitPromotionPrice":"107.000000","isTaxed":1,"quantity":300,"price":"107.000000","taxPrice":15.5465,"taxAmount":4663.95,"netPrice":91.45,"netAmount":27435,"amount":"32100.000000","taxRate":"0.17"},{"skuId":21490,"skuCode":"263690167","productName":"手电钻 GBM10RE 450W 博世","minUnitQuantity":16,"minUnitPurchasingPrice":"289.000000","minUnitPromotionPrice":"289.000000","isTaxed":1,"quantity":16,"price":"289.000000","taxPrice":41.9917,"taxAmount":671.8672,"netPrice":247.01,"netAmount":3952.16,"amount":"4624.000000","taxRate":"0.17"},{"skuId":21493,"skuCode":"263690170","productName":"角磨机 TWS6600 660W 博世","minUnitQuantity":120,"minUnitPurchasingPrice":"139.000000","minUnitPromotionPrice":"139.000000","isTaxed":1,"quantity":120,"price":"139.000000","taxPrice":20.196,"taxAmount":2423.52,"netPrice":118.8,"netAmount":14256,"amount":"16680.000000","taxRate":"0.17"},{"skuId":21494,"skuCode":"263690171","productName":"角磨机 GWS7-100 720W 博世","minUnitQuantity":8,"minUnitPurchasingPrice":"272.000000","minUnitPromotionPrice":"272.000000","isTaxed":1,"quantity":8,"price":"272.000000","taxPrice":39.5216,"taxAmount":316.1728,"netPrice":232.48,"netAmount":1859.84,"amount":"2176.000000","taxRate":"0.17"},{"skuId":21496,"skuCode":"263690173","productName":"电锤 GBH2-22 620W 博世","minUnitQuantity":40,"minUnitPurchasingPrice":"576.000000","minUnitPromotionPrice":"576.000000","isTaxed":1,"quantity":40,"price":"576.000000","taxPrice":83.6927,"taxAmount":3347.708,"netPrice":492.31,"netAmount":19692.4,"amount":"23040.000000","taxRate":"0.17"},{"skuId":21497,"skuCode":"263690174","productName":"电锤 GBH2-26E  800W 博世","minUnitQuantity":50,"minUnitPurchasingPrice":"821.000000","minUnitPromotionPrice":"821.000000","isTaxed":1,"quantity":50,"price":"821.000000","taxPrice":119.2907,"taxAmount":5964.535,"netPrice":701.71,"netAmount":35085.5,"amount":"41050.000000","taxRate":"0.17"},{"skuId":23092,"skuCode":"263691759","productName":"角磨机 GWS 6-100 博世","minUnitQuantity":64,"minUnitPurchasingPrice":"270.000000","minUnitPromotionPrice":"270.000000","isTaxed":1,"quantity":64,"price":"270.000000","taxPrice":39.2309,"taxAmount":2510.7776,"netPrice":230.77,"netAmount":14769.28,"amount":"17280.000000","taxRate":"0.17"},{"skuId":23111,"skuCode":"263691778","productName":"电锤 GBH 4-32 DFR 三功能 调速正反转 电镐/电锤/电钻 博世 900W","minUnitQuantity":4,"minUnitPurchasingPrice":"2190.000000","minUnitPromotionPrice":"2190.000000","isTaxed":1,"quantity":4,"price":"2190.000000","taxPrice":318.2043,"taxAmount":1272.8172,"netPrice":1871.79,"netAmount":7487.16,"amount":"8760.000000","taxRate":"0.17"}]
     */

    private String orderNumber;
    private String orderType;
    private String orderTag;
    private String supplierName;
    private String paymentMethod;
    private String totalAmount;
    private String warehouseName;
    private Integer status;
    private List<ItemsBeanVO> items;
}
