package com.ruigu.rbox.workflow.service.sale;

import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.feign.PassportFeignClient;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.BaseTaskInfoDTO;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.entity.*;
import com.ruigu.rbox.workflow.model.enums.*;
import com.ruigu.rbox.workflow.model.vo.MessageInfoVO;
import com.ruigu.rbox.workflow.model.vo.WorkflowInstanceVO;
import com.ruigu.rbox.workflow.repository.WorkflowDefinitionRepository;
import com.ruigu.rbox.workflow.service.NoticeConfigService;
import com.ruigu.rbox.workflow.service.OperationLogService;
import com.ruigu.rbox.workflow.service.WorkflowInstanceService;
import com.ruigu.rbox.workflow.service.WorkflowTaskService;
import com.ruigu.rbox.workflow.strategy.context.SendNoticeContext;
import com.ruigu.rbox.workflow.supports.ConvertClassUtil;
import com.ruigu.rbox.workflow.supports.NoticeContentUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liqingtian
 * @date 2020/08/12 18:48
 */
@Slf4j
@Service
public class SpecialAfterSaleTaskCreateListener implements TaskListener {

    @Resource
    private WorkflowTaskService taskService;

    @Resource
    private WorkflowInstanceService instanceService;

    @Resource
    private WorkflowDefinitionRepository workflowDefinitionRepository;

    @Resource
    private OperationLogService operationLogService;

    @Resource
    private SendNoticeContext sendNoticeContext;

    @Resource
    private NoticeConfigService noticeConfigService;

    @Resource
    private NoticeContentUtil noticeContentUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notify(DelegateTask delegateTask) {
        log.debug("============================== 进入任务创建监听 ==================================");

        // 查询任务节点信息
        NodeEntity node = taskService.queryNode(delegateTask);
        if (null == node) {
            log.error("异常，节点信息查询失败.TaskDefinitionKey:{},ProcessDefinitionId:{}",
                    delegateTask.getTaskDefinitionKey(),
                    delegateTask.getProcessDefinitionId());
            return;
        }
        // 获取流程定义
        WorkflowDefinitionEntity definition = workflowDefinitionRepository.findById(node.getModelId())
                .orElse(null);
        if (definition == null) {
            log.error("异常。任务获取流程定义实例异常。流程定义信息不存在。");
            return;
        }

        OperationLogEntity logEntity = new OperationLogEntity();
        try {
            Map<String, Object> variables = delegateTask.getVariables();

            // 查询受理人，动态设置
            Integer currentNodeId = (Integer) variables.get(SpecialAfterSaleUseVariableEnum.CURRENT_NODE_ID.getCode());
            String taskApprover = (String) variables.get(SpecialAfterSaleUseVariableEnum.CURRENT_NODE_USER.getCode() + currentNodeId);
            List<Integer> taskApproverIdList = JsonUtil.parseArray(taskApprover, Integer.class);
            if (CollectionUtils.isEmpty(taskApproverIdList)) {
                log.error("任务创建异常，没有找到该任务受理人信息。");
                throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "查询不到下一级审批人");
            }
            List<String> candidateUsers = taskApproverIdList.stream().map(String::valueOf).collect(Collectors.toList());
            delegateTask.addCandidateUsers(candidateUsers);

            // 获取流程实例
            WorkflowInstanceVO instance = instanceService.getInstanceById(delegateTask.getProcessInstanceId());
            String instanceName = String.valueOf(variables.get(InstanceVariableParam.INSTANCE_NAME.getText()));
            if (instance == null) {
                instance = new WorkflowInstanceVO();
                instance.setHistory(false);
                instance.setName(instanceName);
                instance.setCreatedOn(new Date());
            }

            TaskEntity task = taskService.insertTask(delegateTask, node, candidateUsers, definition, instance);
            logEntity.setContent("[ " + definition.getName() + " ] [ " + task.getName() + " ] 任务创建");

            // 判断是否有通知
            List<NoticeTemplateEntity> noticeTemplate = noticeConfigService
                    .getNoticeTemplate(NoticeConfigState.NODE,
                            node.getId(),
                            InstanceEvent.TASK_CREATE.getCode());
            if (CollectionUtils.isEmpty(noticeTemplate)) {
                return;
            }

            // 根据模板 发送通知
            BaseTaskInfoDTO baseTaskInfoDTO = ConvertClassUtil.convertToBaseTaskInfoDTO(task);
            noticeTemplate.forEach(template -> {
                MessageInfoVO message = noticeContentUtil.translateNodeTemplate(template, definition, baseTaskInfoDTO, variables);
                message.setTargets(taskApproverIdList);
                message.setNoticeEventType(InstanceEvent.TASK_CREATE.getCode());
                // 根据渠道、子类型发送不同的通知
                sendNoticeContext.send(template, message);
            });
        } catch (Exception e) {
            log.error("任务创建监听异常：e:{}", e);
            throw new GlobalRuntimeException(ResponseCode.INTERNAL_ERROR.getCode(), e.getMessage());
        } finally {
            // 创建任务记录日志
            Integer userId = UserHelper.getUserId();
            logEntity.setCreatedBy(userId);
            logEntity.setLastUpdatedBy(userId);
            logEntity.setCreatedOn(new Date());
            logEntity.setLastUpdatedOn(new Date());
            logEntity.setTaskId(delegateTask.getId());
            logEntity.setInstanceId(delegateTask.getProcessInstanceId());
            logEntity.setDefinitionId(definition.getId());
            logEntity.setShowStatus(LightningApplyStatus.TO_BE_ACCEPTED.getCode());
            operationLogService.log(logEntity);
        }
    }
}
