package com.ruigu.rbox.workflow.model.request;

import lombok.Data;

/**
 * 搜索流程历史请求
 * @author alan.zhao
 */
@Data
public class SearchHistoryRequest {
    private String name;
    private String definitionId;
    private Integer status;
    private Integer pageIndex;
    private Integer pageSize;
}
