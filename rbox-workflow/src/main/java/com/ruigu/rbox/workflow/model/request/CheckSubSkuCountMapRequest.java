package com.ruigu.rbox.workflow.model.request;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 去库存检验请求
 *
 * @author chenzhenya
 * @date 2019/11/19 20:20
 */
@Data
public class CheckSubSkuCountMapRequest {
    private List<Map<String, Object>> skuList;
    private Integer storageId;
}
