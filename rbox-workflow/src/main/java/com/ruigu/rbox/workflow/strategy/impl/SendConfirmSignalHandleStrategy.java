package com.ruigu.rbox.workflow.strategy.impl;

import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.cloud.kanai.util.TimeUtil;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.bpmn.Button;
import com.ruigu.rbox.workflow.model.dto.TaskCardReturnMessageDTO;
import com.ruigu.rbox.workflow.model.entity.NoticeEntity;
import com.ruigu.rbox.workflow.model.entity.NoticeTemplateEntity;
import com.ruigu.rbox.workflow.model.entity.OperationLogEntity;
import com.ruigu.rbox.workflow.model.entity.WorkflowInstanceEntity;
import com.ruigu.rbox.workflow.model.enums.*;
import com.ruigu.rbox.workflow.model.request.lightning.IssueConfirmReq;
import com.ruigu.rbox.workflow.service.LightningIssueService;
import com.ruigu.rbox.workflow.service.NoticeConfigService;
import com.ruigu.rbox.workflow.service.OperationLogService;
import com.ruigu.rbox.workflow.service.WorkflowInstanceService;
import com.ruigu.rbox.workflow.strategy.TaskCardHandleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/12/31 18:09
 */
@Slf4j
@Component
public class SendConfirmSignalHandleStrategy implements TaskCardHandleStrategy {

    @Resource
    private WorkflowInstanceService workflowInstanceService;

    @Resource
    private NoticeConfigService noticeConfigService;

    @Resource
    private LightningIssueService lightningIssueService;

    @Resource
    private OperationLogService operationLogService;

    @Override
    public ServerResponse handle(TaskCardReturnMessageDTO message, NoticeEntity notice) {
        String errorLogHead = "| - > [ 任务卡片事件处理 ] [ SendConfirmSignalHandleStrategy ] - ";
        // 获取点击的button ,因为是确认
        String instanceId = notice.getInstanceId();
        if (StringUtils.isBlank(instanceId)) {
            return ServerResponse.fail();
        }
        String serverTaskId = "ServiceTask_1841drq";
        String nodeId = notice.getDefinitionId() + Symbol.COLON.getValue() + serverTaskId;
        List<NoticeTemplateEntity> noticeTemplates = noticeConfigService.getNoticeTemplate(NoticeConfigState.NODE,
                nodeId, InstanceEvent.SERVER_TASK.getCode());
        if (CollectionUtils.isEmpty(noticeTemplates)) {
            String errMeg = "异常，找不到通知配置。";
            log.error(errorLogHead + errMeg);
            return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(), errMeg);
        }
        // 查询模板信息
        NoticeTemplateEntity template = noticeTemplates.stream().filter(t -> t.getChannel() == NoticeType.WEIXIN_CHANNEL.getState()
                && t.getType() == NoticeType.TASK_CARD.getState()).findFirst().orElse(null);
        if (template == null) {
            String errMeg = "异常，找不到任务卡片通知配置";
            log.error(errorLogHead + errMeg);
            return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(), errMeg);
        }
        String buttonConfig = template.getButtonConfig();
        if (StringUtils.isBlank(buttonConfig)) {
            String errMeg = "异常，任务卡片按钮配置为空";
            log.error(errorLogHead + errMeg);
            return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(), errMeg);
        }
        // 日志
        OperationLogEntity logEntity = new OperationLogEntity();
        try {
            WorkflowInstanceEntity instance = workflowInstanceService.findInstanceById(instanceId);
            if (instance == null) {
                String errMeg = "异常，查找不到对应流程实例信息";
                log.error(errorLogHead + errMeg);
                return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(), errMeg);
            }
            IssueConfirmReq req = new IssueConfirmReq();
            req.setIssueId(Integer.valueOf(instance.getBusinessKey()));
            Map<String, Object> variables = getVariablesByButtonConfig(buttonConfig, message.getEventKey());
            if (!variables.containsKey(WorkflowStatusFlag.TASK_STATUS.getName())) {
                return ServerResponse.fail();
            }
            Integer status = (Integer) variables.get(WorkflowStatusFlag.TASK_STATUS.getName());
            if (status == null) {
                return ServerResponse.fail();
            }
            if (status.equals(LightningApplyStatus.RESOLVED.getCode())) {
                req.setResolved(true);
                logEntity.setContent("[ 闪电链 ] [ 申请人确认 ] [ 已解决 ]");
                logEntity.setShowStatus(LightningApplyStatus.RESOLVED.getCode());
            } else if (status.equals(LightningApplyStatus.UNRESOLVED.getCode())) {
                logEntity.setContent("[ 闪电链 ] [ 申请人确认 ] [ 未解决 ]");
                req.setResolved(false);
                logEntity.setShowStatus(LightningApplyStatus.UNRESOLVED.getCode());
            }
            // 调用确认方法
            return lightningIssueService.confirmIssue(req);
        } catch (Exception e) {
            String errMsg = "异常，操作失败。";
            log.error(errorLogHead + "{}", e);
            return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(), errMsg);
        } finally {
            Integer operator = Integer.valueOf(notice.getTargets());
            Date now = new Date();
            logEntity.setCreatedBy(operator);
            logEntity.setCreatedOn(now);
            logEntity.setLastUpdatedBy(operator);
            logEntity.setLastUpdatedOn(now);
            logEntity.setInstanceId(notice.getInstanceId());
            logEntity.setDefinitionId(notice.getDefinitionId());
            logEntity.setEvent(InstanceEvent.SERVER_TASK.getCode().toString());
            operationLogService.log(logEntity);
        }
    }

    private Map<String, Object> getVariablesByButtonConfig(String buttonConfig, String clickButtonKey) {
        Map<String, Object> variables = new HashMap<>(16);
        List<Button> buttons = JsonUtil.parseArray(buttonConfig, Button.class);
        buttons.stream().filter(b -> b.getKey().equals(clickButtonKey))
                .findFirst().ifPresent(button -> {
            String key = button.getVarName();
            String value = button.getVarValue();
            Integer type = button.getVarType();
            if (type == DataType.OTHER_DATA.getState()) {
                variables.put(key, value);
            } else if (type == DataType.STRING_DATA.getState()) {
                variables.put(key, value);
            } else if (type == DataType.INTEGER_DATA.getState()) {
                variables.put(key, Integer.valueOf(value));
            } else if (type == DataType.DOUBLE_DATA.getState()) {
                variables.put(key, Double.valueOf(value));
            } else if (type == DataType.DATE_DATA.getState()) {
                variables.put(key, TimeUtil.format(value, TimeUtil.FORMAT_DATE_TIME));
            }
        });
        return variables;
    }
}
