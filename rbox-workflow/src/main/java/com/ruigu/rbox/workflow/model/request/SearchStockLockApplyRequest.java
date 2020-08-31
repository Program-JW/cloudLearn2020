package com.ruigu.rbox.workflow.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 搜索库存锁请求
 * @author heyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchStockLockApplyRequest extends PageableRequest {
    private String storageId;
    private Integer skuCode;
    private Integer createdBy;

}
