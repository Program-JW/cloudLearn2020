package com.ruigu.rbox.workflow.service;

import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/12/25 18:04
 */
public interface VariableService {

    /***
     * 添加流程参数
     * @param instanceId 流程ID
     * @param variables  流程变量
     */
    void addVariable(String instanceId, Map<String, Object> variables);
}
