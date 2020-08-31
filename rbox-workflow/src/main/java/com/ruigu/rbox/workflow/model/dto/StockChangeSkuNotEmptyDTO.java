package com.ruigu.rbox.workflow.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liqingtian
 * @date 2020/02/19 16:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockChangeSkuNotEmptyDTO {
    Integer skuCode;
    Integer storageId;
    Integer count;
}
