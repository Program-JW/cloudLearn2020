package com.ruigu.rbox.workflow.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author ：alan
 * @date ：Created in {2019/9/10} {17:02}
 */
@Data
public class QuerySkuDetailRequest {

    @JsonProperty("product_code")
    private String productCode;

    public QuerySkuDetailRequest(String productCode) {
        this.productCode = productCode;
    }
}
