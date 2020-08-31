package com.ruigu.rbox.workflow.service.listener;

import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.workflow.feign.PassportFeignClient;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.BaseTaskInfoDTO;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.entity.*;
import com.ruigu.rbox.workflow.model.enums.*;
import com.ruigu.rbox.workflow.model.vo.MessageInfoVO;
import com.ruigu.rbox.workflow.model.vo.UserGroupLeaderVO;
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
public class TaskCreateListenerImpl implements TaskListener {
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

    @Override
    public void notify(DelegateTask delegateTask) {
        log.debug("============================== 进入任务创建监听 ==================================");
        OperationLogEntity logEntity = new OperationLogEntity();
        try {
            Map<String, Object> variables = delegateTask.getVariables();
            String instanceName = String.valueOf(variables.get(InstanceVariableParam.INSTANCE_NAME.getText()));
            Long instanceCreatorId = (Long) (variables.get(InstanceVariableParam.INSTANCE_CREATOR_ID.getText()));
            String businessKey = String.valueOf(variables.get(InstanceVariableParam.BUSINESS_KEY.getText()));

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
            List<Integer> groupTargets = new ArrayList<>();
            List<String> usersClone = new ArrayList<>();
            if (node.getCandidateUsers() != null) {
                List<String> users = new ArrayList<>();
                String[] strings = node.getCandidateUsers().split(",");
                for (String userId : strings) {
                    if (CandidateUserType.CREATOR.getState() == Integer.parseInt(userId)) {
                        users.add(instanceCreatorId.toString());
                        usersClone.add(instanceCreatorId.toString());
                        targets.add(instanceCreatorId.intValue());
                    } else if (CandidateUserType.CREATOR_LEADER.getState() == Integer.parseInt(userId)) {
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
                        users.add(userId);
                        usersClone.add(userId);
                        targets.add(Integer.valueOf(userId));
                    }
                }
                delegateTask.addCandidateUsers(users);
            }
            if (node.getCandidateGroups() != null) {
                List<String> groups = new ArrayList<>();
                String[] strings = node.getCandidateGroups().split(",");
                try {
                    groupTargets = userGroupService.getUserListByGroupsInt(Arrays.asList(strings));
                } catch (Exception e) {
                    log.error("异常。获取用户组用户信息异常。", e);
                }
                for (String str : strings) {
                    groups.add(str);
                }
            }
            targets.addAll(groupTargets);
            TaskEntity task = taskService.insertTask(delegateTask, node, usersClone, definition, instance);

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