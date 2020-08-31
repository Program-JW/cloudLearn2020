package com.ruigu.rbox.workflow.service.hrv2;

import com.alibaba.fastjson.JSON;
import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.config.RabbitMqConfig;
import com.ruigu.rbox.workflow.feign.PassportFeignClient;
import com.ruigu.rbox.workflow.feign.handler.HrFeignHandler;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.WorkflowEvent;
import com.ruigu.rbox.workflow.model.dto.BaseTaskInfoDTO;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.entity.*;
import com.ruigu.rbox.workflow.model.enums.*;
import com.ruigu.rbox.workflow.model.request.LeaveReportTaskReq;
import com.ruigu.rbox.workflow.model.vo.MessageInfoVO;
import com.ruigu.rbox.workflow.model.vo.UserGroupLeaderVO;
import com.ruigu.rbox.workflow.model.vo.WorkflowInstanceVO;
import com.ruigu.rbox.workflow.repository.TaskRepository;
import com.ruigu.rbox.workflow.repository.WorkflowDefinitionRepository;
import com.ruigu.rbox.workflow.service.*;
import com.ruigu.rbox.workflow.strategy.context.SendNoticeContext;
import com.ruigu.rbox.workflow.supports.ConvertClassUtil;
import com.ruigu.rbox.workflow.supports.NoticeContentUtil;
import com.ruigu.rbox.workflow.supports.Vars;
import com.ruigu.rbox.workflow.supports.binding.DefaultDestination;
import com.ruigu.rbox.workflow.supports.message.DefaultTxMessage;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author alan.zhao
 */
@SuppressWarnings("ALL")
@Slf4j
@Service
public class LeaveApproveTaskCreateListenerImplV2 implements TaskListener {
    private static final long serialVersionUID = 1L;

    @Resource
    private WorkflowTaskService taskService;

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private WorkflowInstanceService instanceService;

    @Resource
    private WorkflowDefinitionRepository workflowDefinitionRepository;

    @Resource
    private OperationLogService operationLogService;

    @Resource
    private UserGroupService userGroupService;

    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private SendNoticeContext sendNoticeContext;

    @Resource
    private NoticeConfigService noticeConfigService;

    @Resource
    private NoticeContentUtil noticeContentUtil;

    @Resource
    private GuaranteeSuccessMqSender guaranteeSuccessMqSender;

    @Override
    public void notify(DelegateTask delegateTask) {
        log.debug("============================== 进入任务创建监听 ==================================");
        OperationLogEntity logEntity = new OperationLogEntity();
        try {
            Map<String, Object> variables = delegateTask.getVariables();
            String instanceName = String.valueOf(variables.get(InstanceVariableParam.INSTANCE_NAME.getText()));
            Long instanceCreatorId = (Long) (variables.get(InstanceVariableParam.INSTANCE_CREATOR_ID.getText()));
            String businessKey = String.valueOf(variables.get(InstanceVariableParam.BUSINESS_KEY.getText()));
            Integer applyId = (Integer) delegateTask.getVariable("applyId");

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
            // 获取流程实例
            WorkflowInstanceVO instance = instanceService.getInstanceById(delegateTask.getProcessInstanceId());
            if (instance == null) {
                instance = new WorkflowInstanceVO();
                instance.setHistory(false);
                instance.setName(instanceName);
                instance.setCreatedOn(new Date());
            }

            // 构建remarks
            StringBuilder remarks = new StringBuilder(node.getName() + ",");
            remarks.append(instance.getName() + ",");
            remarks.append(definition.getName() + ",");
            if (StringUtils.isNotBlank(businessKey)) {
                remarks.append(businessKey + ",");
            }
            ServerResponse<List<PassportUserInfoDTO>> userInfoResponse = passportFeignClient.getUserMsgByIds(Collections.singletonList(instanceCreatorId.intValue()));
            if (userInfoResponse.getCode() == ResponseCode.SUCCESS.getCode()) {
                List<PassportUserInfoDTO> userList = userInfoResponse.getData();
                if (CollectionUtils.isNotEmpty(userList)) {
                    remarks.append(userList.get(0).getNickname());
                }
            }
            node.setRemarks(remarks.toString());

            List<Integer> targets = new ArrayList<>();
            List<String> usersClone = new ArrayList<>();
            List<Integer> approver = JsonUtil.parseArray(delegateTask.getVariable("approvers").toString(), Integer.class);
            if (CollectionUtils.isNotEmpty(approver)) {
                List<String> users = new ArrayList<>();
                for (Integer userId : approver) {
                    if (CandidateUserType.CREATOR.getState() == userId) {
                        users.add(instanceCreatorId.toString());
                        usersClone.add(instanceCreatorId.toString());
                        targets.add(instanceCreatorId.intValue());
                    } else if (CandidateUserType.CREATOR_LEADER.getState() == userId) {
                        List<String> list = new ArrayList<>();
                        list.add(instanceCreatorId.toString());
                        ServerResponse<List<UserGroupLeaderVO>> response = passportFeignClient.getDeptLeaderInfoList(list);
                        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
                            List<UserGroupLeaderVO> leaders = response.getData();
                            for (UserGroupLeaderVO leaderVO : leaders) {
                                if (leaderVO.getLeaderIds() != null && leaderVO.getLeaderIds().size() > 0) {
                                    for (Integer leaderId : leaderVO.getLeaderIds()) {
                                        users.add(leaderId.toString());
                                        usersClone.add(leaderId.toString());
                                        targets.add(leaderId);
                                    }
                                }
                            }
                        }
                    } else {
                        users.add(userId.toString());
                        usersClone.add(userId.toString());
                        targets.add(Integer.valueOf(userId));
                    }
                }
                delegateTask.addCandidateUsers(users);
            }

            TaskEntity task = taskService.insertTask(delegateTask, node, usersClone, definition, instance);

            Map<String, Object> request = new HashMap<>(5);
            request.put("applyId", applyId);
            request.put("taskId", task.getId());
            request.put("userId", approver);
            request.put("nodeId", Vars.getVar(delegateTask, "nodeId", Integer.class));
            WorkflowEvent event = new WorkflowEvent();
            event.setType(ActivitiEventType.TASK_CREATED);
            event.setData(JSON.toJSONString(request));
            guaranteeSuccessMqSender.send(
                    DefaultDestination.builder().exchangeType(ExchangeTypeEnum.TOPIC)
                            .exchangeName(RabbitMqConfig.WORKFLOW_EVENT_TOPIC_EXCHANGE)
                            .routingKey("rbox.hr.leave-report").build(),
                    DefaultTxMessage.builder()
                            .businessModule("rbox-hr:leave-report")
                            .businessKey(applyId.toString())
                            .content(JSON.toJSONString(event)).build()
            );
            logEntity.setContent("[ " + definition.getName() + " ] [ " + task.getName() + " ] 任务创建");
            logEntity.setDefinitionId(definition.getId());
            if (CollectionUtils.isEmpty(targets)) {
                return;
            }
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
                message.setTargets(targets);
                message.setNoticeEventType(InstanceEvent.TASK_CREATE.getCode());
                // 根据渠道、子类型发送不同的通知
                sendNoticeContext.send(template, message);
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            // 创建任务记录日志
            Integer userId = UserHelper.getUserId();
            logEntity.setCreatedBy(userId);
            logEntity.setLastUpdatedBy(userId);
            logEntity.setCreatedOn(new Date());
            logEntity.setLastUpdatedOn(new Date());
            logEntity.setTaskId(delegateTask.getId());
            logEntity.setInstanceId(delegateTask.getProcessInstanceId());
            logEntity.setEvent(InstanceEvent.TASK_CREATE.getCode().toString());
            operationLogService.log(logEntity);
        }
    }
}