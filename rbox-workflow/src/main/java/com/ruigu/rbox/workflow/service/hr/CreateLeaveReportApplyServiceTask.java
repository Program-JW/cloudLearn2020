package com.ruigu.rbox.workflow.service.hr;

import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.feign.handler.HrFeignHandler;
import com.ruigu.rbox.workflow.model.request.HrLeaveReportApplyReq;
import com.ruigu.rbox.workflow.model.request.LeaveReportCcReq;
import com.ruigu.rbox.workflow.service.DistributedLocker;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 创建申请单
 *
 * @author alan.zhao
 */
@Slf4j
@Service("createLeaveReportApplyServiceTask")
public class CreateLeaveReportApplyServiceTask implements JavaDelegate {

    @Autowired
    private HrFeignHandler hrFeignHandler;

    @Autowired
    private DistributedLocker distributedLocker;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        try {
            int type = Integer.parseInt(delegateExecution.getVariable("type").toString());
            String startTime = (String) delegateExecution.getVariable("startTime");
            String endTime = (String) delegateExecution.getVariable("endTime");
            String startDate = (String) delegateExecution.getVariable("startDate");
            String endDate = (String) delegateExecution.getVariable("endDate");
            Integer start = (Integer) delegateExecution.getVariable("start");
            Integer end = (Integer) delegateExecution.getVariable("end");
            double duration = Double.parseDouble(delegateExecution.getVariable("duration").toString());
            String reason = (String) delegateExecution.getVariable("reason");
            int applyUserId = Integer.parseInt(delegateExecution.getVariable("applyUserId").toString());
            String definitionId = (String) delegateExecution.getVariable("definitionId");
            String instanceId = delegateExecution.getProcessInstanceId();
            List<Integer> ccUsers = JsonUtil.parseArray(delegateExecution.getVariable("ccUsers").toString(), Integer.class);

            HrLeaveReportApplyReq hrLeaveReportApplyReq = new HrLeaveReportApplyReq();
            hrLeaveReportApplyReq.setApplyUserId(applyUserId);
            hrLeaveReportApplyReq.setLeaveReportTypeId(type);
            hrLeaveReportApplyReq.setStartDate(startDate);
            hrLeaveReportApplyReq.setEndDate(endDate);
            hrLeaveReportApplyReq.setStart(start);
            hrLeaveReportApplyReq.setEnd(end);
            hrLeaveReportApplyReq.setStartTime(startTime);
            hrLeaveReportApplyReq.setEndTime(endTime);
            hrLeaveReportApplyReq.setDuration(duration);
            hrLeaveReportApplyReq.setDefinitionId(definitionId);
            hrLeaveReportApplyReq.setInstanceId(instanceId);
            hrLeaveReportApplyReq.setStatus(0);
            hrLeaveReportApplyReq.setCreatedBy(applyUserId);
            hrLeaveReportApplyReq.setLastUpdateBy(applyUserId);
            hrLeaveReportApplyReq.setApplyReason(reason);
            hrLeaveReportApplyReq.setCcIdList(ccUsers);
            Integer applyId = hrFeignHandler.recordLeaveReportApply(hrLeaveReportApplyReq);

            delegateExecution.setVariable("applyId", applyId, true);

            // 发送抄送通知
            LeaveReportCcReq reportCcReq = new LeaveReportCcReq();
            reportCcReq.setApplyId(applyId);
            reportCcReq.setUserIds(ccUsers);
            hrFeignHandler.sendLeaveApplyCcNotice(reportCcReq);

            log.info("[type={},startTime={},endTime={},duration={}小时,reason={},applyId={}]", type, startTime, endTime, duration, reason, applyId);
        } finally {
            log.info("创建请假报备记录");
        }
    }
}
