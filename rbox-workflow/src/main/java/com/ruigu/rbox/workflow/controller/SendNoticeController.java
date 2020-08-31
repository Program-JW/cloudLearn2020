package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.BaseTaskInfoDTO;
import com.ruigu.rbox.workflow.model.entity.NoticeTemplateEntity;
import com.ruigu.rbox.workflow.model.entity.TaskEntity;
import com.ruigu.rbox.workflow.model.entity.WorkflowDefinitionEntity;
import com.ruigu.rbox.workflow.model.enums.NoticeConfigState;
import com.ruigu.rbox.workflow.model.enums.InstanceEvent;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.enums.TaskState;
import com.ruigu.rbox.workflow.model.request.EnvelopeReq;
import com.ruigu.rbox.workflow.model.vo.MessageInfoVO;
import com.ruigu.rbox.workflow.model.vo.WorkflowInstanceVO;
import com.ruigu.rbox.workflow.service.*;
import com.ruigu.rbox.workflow.strategy.context.SendNoticeContext;
import com.ruigu.rbox.workflow.supports.ConvertClassUtil;
import com.ruigu.rbox.workflow.supports.NoticeContentUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.payloads.GetVariablesPayload;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author liqingtian
 * @date 2019/10/18 11:27
 */
@Slf4j
@RestController
@RequestMapping("/send")
public class SendNoticeController {

    @Resource
    private NoticeConfigService noticeConfigService;

    @Resource
    private TaskService taskService;

    @Resource
    private WorkflowTaskService workflowTaskService;

    @Resource
    private ProcessRuntime processRuntime;

    @Resource
    private UserGroupService userGroupService;

    @Resource
    private WorkflowInstanceService workflowInstanceService;

    @Resource
    private WorkflowDefinitionService workflowDefinitionService;

    @Resource
    private SendNoticeContext sendNoticeContext;

    @Resource
    private NoticeContentUtil noticeContentUtil;

    @Resource
    private QuestNoticeService questNoticeService;

    public ServerResponse send(NoticeConfigState noticeConfigState, Integer event, String id) {
        switch (noticeConfigState) {
            case NODE:
                return sendNodeTypeNotice(event, id);
            case DEFINITION:
                // 查询流程通知配置
                return sendInstanceTypeNotice(event, id);
            default:
                break;
        }
        return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), "没有找到相应类型通知。");
    }

    private ServerResponse sendNodeTypeNotice(Integer event, String id) {
        // 查询任务通知配置
        TaskEntity taskEntity = workflowTaskService.getTaskEntityById(id);
        if (taskEntity == null) {
            return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), "未找到该任务详细信息");
        }
        if (taskEntity.getStatus() == TaskState.COMPLETED.getState()) {
            return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), "已完成或已审批的任务不允许再次发送通知。");
        }

        // 查询流程定义
        WorkflowDefinitionEntity definition = workflowDefinitionService.getDefinitionEntityById(id);
        if (definition == null) {
            return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), "未找到该任务流程定义信息。");
        }
        // 查询流程实例
        WorkflowInstanceVO instance = workflowInstanceService.getInstanceById(taskEntity.getInstanceId());
        if (instance == null) {
            return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), "未找到该任务流程信息。");
        }

        // 通知送达人
        Set<Integer> targets = new HashSet<>();
        if (InstanceEvent.TASK_COMPLETE.getCode().equals(event)) {
            targets.add(instance.getCreatedBy().intValue());
            if (instance.getOwnerId() != null) {
                targets.add(instance.getOwnerId().intValue());
            }
        } else {
            if (StringUtils.isNotBlank(taskEntity.getCandidateUsers())) {
                String[] users = taskEntity.getCandidateUsers().split(",");
                for (String user : users) {
                    targets.add(Integer.valueOf(user));
                }
            }
            if (StringUtils.isNotBlank(taskEntity.getCandidateGroups())) {
                String[] groups = taskEntity.getCandidateUsers().split(",");
                List<Integer> grouptargets = userGroupService.getUserListByGroupsInt(Arrays.asList(groups));
                targets.addAll(grouptargets);
            }
        }

        if (CollectionUtils.isEmpty(targets)) {
            return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), "通知送达人为空.");
        }

        Map<String, Object> variables = new HashMap<>(16);
        try {
            Task task = taskService.createTaskQuery().taskId(taskEntity.getId()).singleResult();
            if (task != null) {
                variables = task.getProcessVariables();
            }
        } catch (Exception e) {
            log.error("异常：{}", e);
            return ServerResponse.fail(e.getMessage());
        }

        String nodeId = taskEntity.getNodeId();
        List<NoticeTemplateEntity> noticeTemplate = noticeConfigService.getNoticeTemplate(NoticeConfigState.NODE, nodeId, event);
        Map<String, Object> finalVariables = variables;
        BaseTaskInfoDTO baseTaskInfoDTO = ConvertClassUtil.convertToBaseTaskInfoDTO(taskEntity);
        noticeTemplate.forEach(template -> {
            MessageInfoVO message = noticeContentUtil.translateNodeTemplate(template, definition, baseTaskInfoDTO, finalVariables);
            message.setTargets(targets);
            message.setNoticeEventType(event);
            sendNoticeContext.send(template, message);
        });

        return ServerResponse.ok();
    }

    private ServerResponse sendInstanceTypeNotice(Integer event, String id) {
        WorkflowInstanceVO instance = workflowInstanceService.getInstanceById(id);
        if (instance == null) {
            return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), "未找到该任务流程信息。");
        }
        if (instance.getHistory()) {
            return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), "已完成流程不允许再次通知。");
        }
        WorkflowDefinitionEntity definition = workflowDefinitionService.getDefinitionEntityById(instance.getDefinitionId());
        if (definition == null) {
            return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), "未找到该任务流程定义信息。");
        }

        // 流程变量
        List<VariableInstance> variableList = processRuntime.variables(new GetVariablesPayload(instance.getId()));
        Map<String, Object> variables = new HashMap<>(16);
        variableList.forEach(variable -> {
            variables.put(variable.getName(), variable.getValue());
        });
        // 送达人
        Set<Integer> target = new HashSet<>();
        target.add(instance.getCreatedBy().intValue());

        String instanceId = instance.getId();
        List<NoticeTemplateEntity> noticeTemplate = noticeConfigService.getNoticeTemplate(NoticeConfigState.DEFINITION, id, event);
        noticeTemplate.forEach(template -> {
            MessageInfoVO message = noticeContentUtil.translateDefinitionTemplate(template, definition, instanceId, variables);
            message.setNoticeEventType(event);
            message.setTargets(target);
            sendNoticeContext.send(template, message);
        });

        return ServerResponse.ok();
    }

    @PostMapping("/mq")
    public ServerResponse sendNoticeToMq(@RequestBody List<EnvelopeReq> envelopes) {
        return questNoticeService.sendNoticeByMq(envelopes);
    }
}
