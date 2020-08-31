package com.ruigu.rbox.workflow.service.hrv2;

import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.service.RemoteService;
import com.ruigu.rbox.workflow.supports.Vars;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 考勤报备流程V2,获取下一个审批节点配置
 *
 * @author alan.zhao
 */
@Slf4j
@Service("nextLeaveReportRuleServiceTaskV2")
public class NextLeaveReportRuleServiceTaskV2 implements JavaDelegate {

    @Resource
    private RemoteService remoteService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        try {
            Integer applyId = Vars.getVar(delegateExecution, "applyId", Integer.class);
            Integer nodeId = Vars.getVar(delegateExecution, "nodeId", Integer.class);
            Map<String, Object> param = new HashMap<>(5);
            param.put("applyId", applyId);
            param.put("nodeId", nodeId);
            log.info("获取下一个审批节点,applyId={},nodeId={}", applyId, nodeId);
            ServerResponse<Object> response = remoteService.request("GET", "http://rbox-hr/leaveApply/{applyId}/next-node?nodeId={nodeId}", param);
            if (!response.isSuccess()) {
                throw new GlobalRuntimeException(ResponseCode.INTERNAL_ERROR.getCode(), response.getMessage());
            }
            if (response.getData() == null) {
                delegateExecution.setVariable("hasNext", 0);
            } else {
                delegateExecution.setVariable("hasNext", 1);
                Map<String, Object> nodeConfig = (Map<String, Object>) response.getData();
                delegateExecution.setVariable("nodeId", nodeConfig.get("nodeId"));
                delegateExecution.setVariable("approvers", nodeConfig.get("candidateUserIds"));
            }
            delegateExecution.setVariable("fetchNextFail", 0);
        } catch (Throwable e) {
            delegateExecution.setVariable("fetchNextFail", 1);
            log.error("", e);
        }
    }
}