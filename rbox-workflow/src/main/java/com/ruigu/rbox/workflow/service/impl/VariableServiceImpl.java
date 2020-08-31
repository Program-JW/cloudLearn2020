package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.service.VariableService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/12/25 18:06
 */
@Slf4j
@Service
public class VariableServiceImpl implements VariableService {

    @Resource
    private RuntimeService runtimeService;

    @Override
    public void addVariable(String instanceId, Map<String, Object> variables) {
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(instanceId).list();
        if (CollectionUtils.isNotEmpty(executions)) {
            try {
                executions.forEach(execution -> runtimeService.setVariables(execution.getId(), variables));
            } catch (Exception e) {
                log.error("流程变量添加异常：{}", e);
                throw new GlobalRuntimeException(ResponseCode.INTERNAL_ERROR.getCode(), "操作失败，流程变量保存失败");
            }
        } else {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "实例ID查询异常，系统中查询不到该实例ID信息");
        }
    }
}
