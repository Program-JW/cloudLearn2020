package com.ruigu.rbox.workflow.service.listener;

import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.model.dto.BaseTaskInfoDTO;
import com.ruigu.rbox.workflow.model.entity.*;
import com.ruigu.rbox.workflow.model.enums.NoticeConfigState;
import com.ruigu.rbox.workflow.model.enums.InstanceEvent;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.enums.WorkflowStatusFlag;
import com.ruigu.rbox.workflow.model.vo.MessageInfoVO;
import com.ruigu.rbox.workflow.model.vo.WorkflowInstanceVO;
import com.ruigu.rbox.workflow.repository.TaskRepository;
import com.ruigu.rbox.workflow.repository.WorkflowDefinitionRepository;
import com.ruigu.rbox.workflow.service.*;
import com.ruigu.rbox.workflow.strategy.context.SendNoticeContext;
import com.ruigu.rbox.workflow.supports.ConvertClassUtil;
import com.ruigu.rbox.workflow.supports.NoticeContentUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author liqingtian
 * @date 2019/08/21 15:33
 */
@Slf4j
@Service
public class TaskCompleteListenerImpl implements TaskListener {

    private static final long serialVersionUID = 1L;

    @Resource
    private OperationLogService operationLogService;

    @Resource
    private NoticeConfigService noticeConfigService;

    @Resource
    private WorkflowDefinitionRepository workflowDefinitionRepository;

    @Resource
    private WorkflowInstanceService instanceService;

    @Resource
    private WorkflowTaskService taskService;

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private SendNoticeContext sendNoticeContext;

    @Resource
    private NoticeContentUtil noticeContentUtil;

    @Override
    public void notify(DelegateTask delegateTask) {
        log.debug("============================== 进入任务提交监听 ==================================");
        OperationLogEntity logEntity = new OperationLogEntity();
        Map<String, Object> variables = delegateTask.getVariables();
        Integer taskStatus = null;
        if (variables.containsKey(WorkflowStatusFlag.TASK_STATUS.getName())) {
            taskStatus = (Integer) variables.get(WorkflowStatusFlag.TASK_STATUS.getName());
        }
        try {
            NodeEntity node = taskService.queryNode(delegateTask);
            if (null == node) {
                log.error("异常，节点信息查询失败.TaskDefinitionKey:{},ProcessDefinitionId:{}",
                        delegateTask.getTaskDefinitionKey(),
                        delegateTask.getProcessDefinitionId());
                return;
            }
            // 获取流程定义
            WorkflowDefinitionEntity definition = workflowDefinitionRepository.findById(node.getModelId())
                    .orElseThrow(() -> new VerificationFailedException(ResponseCode.INTERNAL_ERROR.getCode(), "异常。任务获取流程定义实例异常。流程定义信息不存在。"));
            logEntity.setContent("[ " + definition.getName() + " ] [ " + node.getName() + " ] 任务提交");
            logEntity.setDefinitionId(definition.getId());

            // 判断是否有通知
            List<NoticeTemplateEntity> noticeTemplate = noticeConfigService
                    .getNoticeTemplate(NoticeConfigState.NODE,
                            node.getId(),
                            InstanceEvent.TASK_COMPLETE.getCode());
            if (CollectionUtils.isEmpty(noticeTemplate)) {
                return;
            }
            // 获取流程实例
            WorkflowInstanceVO instance = instanceService.getInstanceById(delegateTask.getProcessInstanceId());
            if (instance == null) {
                log.error("任务提交监听：流程实例查询失败。");
                return;
            }

            // 送达人
            Set<Integer> targets = new HashSet<>();
            targets.add(instance.getCreatedBy().intValue());
            if (instance.getOwnerId() != null) {
                targets.add(instance.getOwnerId().intValue());
            }
            if (CollectionUtils.isEmpty(targets)) {
                return;
            }
            // 获取任务实例
            TaskEntity task = taskRepository.findById(delegateTask.getId())
                    .orElseThrow(() -> new VerificationFailedException(ResponseCode.INTERNAL_ERROR.getCode(),
                            "任务提交监听：提交任务查询失败。"));

            // 根据模板 发送通知
            BaseTaskInfoDTO baseTaskInfoDTO = ConvertClassUtil.convertToBaseTaskInfoDTO(task);
            noticeTemplate.forEach(template -> {
                MessageInfoVO message = noticeContentUtil.translateNodeTemplate(template, definition, baseTaskInfoDTO, variables);
                message.setTargets(targets);
                message.setNoticeEventType(InstanceEvent.TASK_COMPLETE.getCode());
                // 根据渠道、子类型发送不同的通知
                sendNoticeContext.send(template, message);
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            // 记录日志
            Integer userId = UserHelper.getUserId();
            logEntity.setEvent(InstanceEvent.TASK_COMPLETE.getCode().toString());
            logEntity.setShowStatus(taskStatus);
            logEntity.setCreatedBy(userId);
            logEntity.setLastUpdatedBy(userId);
            logEntity.setCreatedOn(new Date());
            logEntity.setLastUpdatedOn(new Date());
            logEntity.setTaskId(delegateTask.getId());
            logEntity.setInstanceId(delegateTask.getProcessInstanceId());
            operationLogService.log(logEntity);
        }
    }
}
