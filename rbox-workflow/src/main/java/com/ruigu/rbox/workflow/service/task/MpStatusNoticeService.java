package com.ruigu.rbox.workflow.service.task;

import com.alibaba.fastjson.JSONObject;
import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.feign.MpClient;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.entity.NoticeEntity;
import com.ruigu.rbox.workflow.model.entity.OperationLogEntity;
import com.ruigu.rbox.workflow.model.entity.WorkflowDefinitionEntity;
import com.ruigu.rbox.workflow.model.enums.InstanceEvent;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.enums.TaskSubmitState;
import com.ruigu.rbox.workflow.model.request.UpdatePurchaseOrderStatusRequest;
import com.ruigu.rbox.workflow.model.vo.WorkflowInstanceVO;
import com.ruigu.rbox.workflow.repository.WorkflowDefinitionRepository;
import com.ruigu.rbox.workflow.service.NoticeLogService;
import com.ruigu.rbox.workflow.service.OperationLogService;
import com.ruigu.rbox.workflow.service.QuestNoticeService;
import com.ruigu.rbox.workflow.service.WorkflowInstanceService;
import com.ruigu.rbox.workflow.supports.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author liqingtian
 * @date 2019/09/18 19:13
 */
@Slf4j
@Service("mpNoticeServiceTask")
public class MpStatusNoticeService implements JavaDelegate {

    @Autowired
    private MpClient mpClient;

    @Autowired
    private WorkflowDefinitionRepository workflowDefinitionRepository;

    @Autowired
    private WorkflowInstanceService workflowInstanceService;

    @Autowired
    private QuestNoticeService questNoticeService;

    @Autowired
    private NoticeLogService noticeLogService;

    @Autowired
    private OperationLogService operationLogService;

    private final String LAST_APPROVAL = "lastApproval";

    @Override
    public void execute(DelegateExecution delegateExecution) {
        log.error("================================ 通知MP =================================");
        OperationLogEntity logEntity = new OperationLogEntity();
        NoticeEntity notice = new NoticeEntity();
        try {
            WorkflowInstanceVO instance = workflowInstanceService.getInstanceById(delegateExecution.getProcessInstanceId());
            if (instance == null) {
                throw new GlobalRuntimeException(400, "异常。流程实例查询异常。无法获取流程信息");
            }
            // 发送流程结束通知
            WorkflowDefinitionEntity definition = workflowDefinitionRepository.latestReleased(instance.getDefinitionCode());
            if (definition == null) {
                throw new GlobalRuntimeException(400, "异常。流程定义查询异常。无法获取流程定义信息");
            }
            logEntity.setDefinitionId(definition.getId());
            notice.setDefinitionId(definition.getId());

            boolean isPass;
            UpdatePurchaseOrderStatusRequest req = new UpdatePurchaseOrderStatusRequest();
            req.setOrderNumber(delegateExecution.getProcessInstanceBusinessKey());
            Map<String, Object> variables = delegateExecution.getVariables();
            Integer approvalValue = Integer.parseInt(String.valueOf(variables.get(LAST_APPROVAL)));
            if (variables.containsKey(LAST_APPROVAL) && TaskSubmitState.PASS.getCode() == approvalValue) {
                log.debug("审批通过。");
                req.setIsPass(TaskSubmitState.PASS.getCode());
                isPass = true;
            } else {
                log.debug("审批驳回。");
                req.setIsPass(TaskSubmitState.REJECT.getCode());
                isPass = false;
            }
            ServerResponse<Object> response = mpClient.updatePurchaseOrderStatus(req);
            if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
                logEntity.setContent("[ 审批已完成 ] " + (isPass ? "通过" : "驳回"));
                log.debug("通知MP成功");
            } else {
                logEntity.setContent("[ MP系统：状态修改失败 ]");
                log.error("失败。通知MP失败。返回信息 --> " + JSONObject.toJSONString(response));
            }

            String businessUrl = definition.getInitialUrl();
            String noticeContent = "您的" + definition.getName() + "已"
                    + (isPass ? "通过" : "驳回")
                    + "。\n点击查看";
            notice.setContent(noticeContent);
            String url = getInstanceInfoUrl(businessUrl, instance);
            notice.setInstanceId(url);
            notice.setTitle(instance.getName());
            Set<Integer> targets = new HashSet<>();
            targets.add(instance.getOwnerId().intValue());
            targets.add(instance.getCreatedBy().intValue());
            notice.setTargets(StringUtils.join(targets, ","));
            notice.setCreatedOn(new Date());
            log.debug("============================= 流程结束通知 =========================== ");
            ServerResponse serverResponse = questNoticeService.sendWeiXinTextNotice(url, noticeContent, targets);
            if (serverResponse.getCode() == ResponseCode.SUCCESS.getCode()) {
                notice.setStatus((byte) 1);
            } else {
                notice.setStatus((byte) 0);
            }
        } catch (Exception e) {
            log.error("mp订单状态更改异常：", e);
        } finally {
            Integer userId = UserHelper.getUserId();
            logEntity.setCreatedBy(userId);
            logEntity.setLastUpdatedBy(userId);
            logEntity.setCreatedOn(new Date());
            logEntity.setLastUpdatedOn(new Date());
            logEntity.setInstanceId(delegateExecution.getProcessInstanceId());
            logEntity.setTaskId(String.valueOf(InstanceEvent.SERVER_TASK.getCode()));
            logEntity.setStatus(1);
            logEntity.setEvent("upMpStatus");
            operationLogService.log(logEntity);

            notice.setType(InstanceEvent.SERVER_TASK.getCode());
            notice.setInstanceId(delegateExecution.getProcessInstanceId());
            noticeLogService.insertNotice(notice);
        }
    }

    private String getInstanceInfoUrl(String url, WorkflowInstanceVO instance) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        String businessKey = instance.getBusinessKey();
        if (StringUtils.isBlank(businessKey)) {
            return null;
        }
        String instanceId = instance.getId();
        if (StringUtils.isBlank(instanceId)) {
            return null;
        }
        return UrlUtil.setBusinessAndInstanceParam(url, businessKey, instanceId);
    }
}
