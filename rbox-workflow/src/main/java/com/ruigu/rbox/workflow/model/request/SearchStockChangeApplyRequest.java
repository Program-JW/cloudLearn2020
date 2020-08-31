package com.ruigu.rbox.workflow.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 搜索库存调整申请请求
 * @author alan.zhao
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchStockChangeApplyRequest extends PageableRequest {
    private String createdBy;
    private String skuCode;
    private String storageId;
    private String unbalanced;
    private String paginated ;
    private String orders;
}
