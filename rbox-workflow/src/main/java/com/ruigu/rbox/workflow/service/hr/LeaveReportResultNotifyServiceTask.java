package com.ruigu.rbox.workflow.service.hr;

import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.feign.handler.HrFeignHandler;
import com.ruigu.rbox.workflow.model.request.HrLeaveReportApplyReq;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author alan.zhao
 */
@Slf4j
@Service("leaveReportResultNotifyServiceTask")
public class LeaveReportResultNotifyServiceTask implements JavaDelegate {

    @Autowired
    private HrFeignHandler hrFeignHandler;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        int approved = Integer.parseInt(delegateExecution.getVariable("approved").toString());
        List<Integer> approver = JsonUtil.parseArray(delegateExecution.getVariable("approver").toString(), Integer.class);
        Integer applyId = (Integer) delegateExecution.getVariable("applyId");
        int status;
        if (approved == 1) {
            status = 3;
        } else {
            status = 4;
        }
        HrLeaveReportApplyReq req = new HrLeaveReportApplyReq();
        req.setId(applyId);
        req.setStatus(status);
        hrFeignHandler.updateApplyTask(req);

        log.info("审批结果:" + JsonUtil.toJsonString(approver) + (approved == 1 ? "已同意" : "已驳回"));
    }
}
