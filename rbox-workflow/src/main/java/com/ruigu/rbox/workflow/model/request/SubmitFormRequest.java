package com.ruigu.rbox.workflow.model.request;

import lombok.Data;

import java.util.Map;

/**
 * 提交表单请求
 * @author alan.zhao
 */
@Data
public class SubmitFormRequest {
    private Map<String,Object> variables;
    private String categoryId;
}
