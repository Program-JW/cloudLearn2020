package com.ruigu.rbox.workflow.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liqingtian
 * @date 2020/02/19 17:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockChangeLastApplyRequest {

    Integer skuCode;
    Integer storageId;
}
