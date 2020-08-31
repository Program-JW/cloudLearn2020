package com.ruigu.rbox.workflow.model.request;

import lombok.Data;

/**
 * 下拉选择请求
 * @author alan.zhao
 */
@Data
public class SelectRequest {
    private String ids;
    private String key;
    private Integer top;
}
