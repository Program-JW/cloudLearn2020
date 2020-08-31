package com.ruigu.rbox.workflow.service.hr;


import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.model.dto.BaseTaskInfoDTO;
import com.ruigu.rbox.workflow.model.entity.*;
import com.ruigu.rbox.workflow.model.enums.InstanceEvent;
import com.ruigu.rbox.workflow.model.enums.NoticeConfigState;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author alan.zhao
 */
@Slf4j
@Service
public class LeaveApproveTaskDeleteListenerImpl implements TaskListener {

    private static final long serialVersionUID = -2353362566754781715L;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private NoticeConfigService noticeConfigService;

    @Autowired
    private WorkflowDefinitionRepository workflowDefinitionRepository;

    @Autowired
    private WorkflowInstanceService instanceService;

    @Autowired
    private NoticeContentUtil noticeContentUtil;

    @Autowired
    private WorkflowTaskService taskService;

    @Autowired
    private SendNoticeContext sendNoticeContext;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public void notify(DelegateTask delegateTask) {
        log.debug("============================== 进入任务删除监听 ==================================");
        OperationLogEntity logEntity = new OperationLogEntity();
        try {
            Map<String, Object> variables = delegateTask.getVariables();
            NodeEntity node = taskService.queryNode(delegateTask);
            if (null == node) {
                log.error("异常，节点信息查询失败.TaskDefinitionKey:{},ProcessDefinitionId:{}",
                        delegateTask.getTaskDefinitionKey(),
                        delegateTask.getProcessDefinitionId());
                return;
            }
            // 获取流程定义
            WorkflowDefinitionEntity definition = workflowDefinitionRepository.findById(node.getModelId())
                    .orElseThrow(() -> new VerificationFailedException(400, "异常。任务获取流程定义实例异常。流程定义信息不存在。"));

            logEntity.setContent("[ " + definition.getName() + " ] [ " + node.getName() + " ] 任务删除");
            logEntity.setDefinitionId(definition.getId());

            // 判断是否有通知
            List<NoticeTemplateEntity> noticeTemplate = noticeConfigService
                    .getNoticeTemplate(NoticeConfigState.NODE,
                            node.getId(),
                            InstanceEvent.TASK_DELETE.getCode());
            if (CollectionUtils.isEmpty(noticeTemplate)) {
                return;
            }

            Set<Integer> targets = new HashSet<>();
            if (StringUtils.isNotBlank(node.getCandidateUsers())) {
                String[] users = node.getCandidateUsers().split(",");
                for (String user : users) {
                    targets.add(Integer.valueOf(user));
                }
            }
            if (StringUtils.isNotBlank(node.getCandidateGroups())) {
                String[] groups = node.getCandidateUsers().split(",");
                List<Integer> grouptargets = userGroupService.getUserListByGroupsInt(Arrays.asList(groups));
                targets.addAll(grouptargets);
            }
            if (CollectionUtils.isEmpty(targets)) {
                return;
            }

            TaskEntity task = taskRepository.findById(delegateTask.getId()).orElse(null);
            if (task == null) {
                return;
            }
            // 获取流程实例
            WorkflowInstanceVO instance = instanceService.getInstanceById(delegateTask.getProcessInstanceId());
            if (instance == null) {
                log.error("任务删除监听：流程实例查找失败。");
                return;
            }

            // 根据模板 发送通知
            BaseTaskInfoDTO baseTaskInfoDTO = ConvertClassUtil.convertToBaseTaskInfoDTO(task);
            noticeTemplate.forEach(template -> {
                // 将title 和 content转义
                MessageInfoVO message = noticeContentUtil.translateNodeTemplate(template, definition, baseTaskInfoDTO, variables);
                // 根据渠道、子类型发送不同的通知
                message.setTargets(targets);
                message.setNoticeEventType(InstanceEvent.TASK_DELETE.getCode());
                sendNoticeContext.send(template, message);
            });
        } catch (Exception e) {
            log.error("任务删除监听异常", e);
        } finally {
            Integer userId = UserHelper.getUserId();
            logEntity.setCreatedBy(userId);
            logEntity.setLastUpdatedBy(userId);
            logEntity.setCreatedOn(new Date());
            logEntity.setLastUpdatedOn(new Date());
            logEntity.setTaskId(delegateTask.getId());
            logEntity.setInstanceId(delegateTask.getProcessInstanceId());
            logEntity.setEvent(InstanceEvent.TASK_DELETE.getCode().toString());
            operationLogService.log(logEntity);
        }
    }
}
