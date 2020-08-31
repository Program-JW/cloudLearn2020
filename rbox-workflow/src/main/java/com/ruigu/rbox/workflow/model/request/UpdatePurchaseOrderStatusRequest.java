package com.ruigu.rbox.workflow.model.request;

import lombok.Data;

/**
 * 更新采购单状态请求
 * @author alan.zhao
 */
@Data
public class UpdatePurchaseOrderStatusRequest {
    private String orderNumber;
    private Integer isPass;

    public UpdatePurchaseOrderStatusRequest() {
    }

    public UpdatePurchaseOrderStatusRequest(String orderNumber, Integer isPass) {
        this.orderNumber = orderNumber;
        this.isPass = isPass;
    }
}
