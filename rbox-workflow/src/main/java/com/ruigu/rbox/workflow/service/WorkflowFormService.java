package com.ruigu.rbox.workflow.service;

/**
 * @author liqingtian
 * @date 2019/10/11 13:54
 */
public interface WorkflowFormService {

    /**
     * 获取查询条件json
     *
     * @param definitionId 定义id
     * @return 查询json
     */
    String getSelectFormContentByDefinition(String definitionId);
}
