package com.ruigu.rbox.workflow.service.stock;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import java.util.Map;

/**
 * @author chenzhenya
 * @date 2019/11/14 10:50
 */
@Slf4j
public abstract class BaseUpdateApplyApprovalTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        log.debug("========================================= 更改申请单审批状态 ==============================");
        Map<String, Object> variables = delegateExecution.getVariables();
        String approvalParamName = "approved";
        if (variables.containsKey(approvalParamName)) {
            // 查询 申请记录
            try {
                updateStatus(delegateExecution);
            } catch (Exception e) {
                log.error("|- 更改申请单审批状态异常。", e);
            }
        }
    }

    /**
     * 更改审批状态
     * @param delegateExecution
     */
    public abstract void updateStatus(DelegateExecution delegateExecution);
}
