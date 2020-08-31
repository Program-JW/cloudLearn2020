package com.ruigu.rbox.workflow.service.hr;

import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author alan.zhao
 */
@Slf4j
@Service("nextLeaveReportRuleServiceTask")
public class NextLeaveReportRuleServiceTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        try {
            Map<String, Object> reviewUserConfigMap = JsonUtil.parseMap(delegateExecution.getVariable("approvers").toString());
            int currentOrder = (int) delegateExecution.getVariable("currentOrder");
            currentOrder = currentOrder + 1;
            if (currentOrder <= reviewUserConfigMap.size()) {
                delegateExecution.setVariable("currentOrder", currentOrder);
                delegateExecution.setVariable("approver", reviewUserConfigMap.get(String.valueOf(currentOrder)));
            } else {
                delegateExecution.setVariable("hasNext", 0);
            }
        } finally {
            log.info("切换下一个请假规则");
        }
    }
}
