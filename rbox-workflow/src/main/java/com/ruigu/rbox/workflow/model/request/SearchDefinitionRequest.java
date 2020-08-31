package com.ruigu.rbox.workflow.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 搜索流程定义请求
 * @author alan.zhao
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchDefinitionRequest extends PageableRequest{
    private String key;
}
