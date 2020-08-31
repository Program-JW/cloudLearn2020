package com.ruigu.rbox.workflow.service.hrv2;

import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.enums.InstanceVariableParam;
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
 * 考勤报备流程V2,同步审批结果给
 *
 * @author alan.zhao
 */
@Slf4j
@Service("leaveReportResultNotifyServiceTaskV2")
public class LeaveReportResultNotifyServiceTaskV2 implements JavaDelegate {

    @Resource
    private RemoteService remoteService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        try {
            Integer approved = Vars.getVar(delegateExecution, "approved", Integer.class);
            Integer applyId = Vars.getVar(delegateExecution, "applyId", Integer.class);
            String definitionId = Vars.getVar(delegateExecution, InstanceVariableParam.DEFINITION_ID.getText(), String.class);
            String instanceId = delegateExecution.getProcessInstanceId();
            int status = approved == 1 ? 3 : 4;
            Map<String, Object> param = new HashMap<>(5);
            param.put("id", applyId);
            param.put("status", status);
            ServerResponse<Object> response = remoteService.request("PUT", "http://rbox-hr/leaveApply/update", param);
            if (!response.isSuccess()) {
                log.error("考勤报备流程[申请ID={},流程ID={},实例ID={}],审批结果:{},同步失败", applyId, definitionId, instanceId, (approved == 1 ? "已同意" : "已驳回"));
                throw new GlobalRuntimeException(ResponseCode.INTERNAL_ERROR.getCode(), response.getMessage());
            } else {
                log.info("考勤报备流程[申请ID={},流程ID={},实例ID={}],审批结果:{},同步成功", applyId, definitionId, instanceId, (approved == 1 ? "已同意" : "已驳回"));
            }
            delegateExecution.setVariable("resultNotifyFail", 0);
        } catch (Throwable e) {
            delegateExecution.setVariable("resultNotifyFail", 1);
        }

    }
}
