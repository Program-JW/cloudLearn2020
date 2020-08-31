package com.ruigu.rbox.workflow.strategy.impl;

import com.ruigu.rbox.workflow.feign.PassportFeignClient;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.dto.TaskCardReturnMessageDTO;
import com.ruigu.rbox.workflow.model.entity.*;
import com.ruigu.rbox.workflow.model.enums.*;
import com.ruigu.rbox.workflow.repository.LightningIssueGroupRepository;
import com.ruigu.rbox.workflow.repository.TaskRepository;
import com.ruigu.rbox.workflow.service.ChatWebSocketService;
import com.ruigu.rbox.workflow.service.WorkflowInstanceService;
import com.ruigu.rbox.workflow.strategy.TaskCardHandleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author liqingtian
 * @date 2020/01/02 22:15
 */
@Slf4j
@Component
public class SendUrgeNoticeHandleStrategy implements TaskCardHandleStrategy {

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private WorkflowInstanceService workflowInstanceService;

    @Resource
    private LightningIssueGroupRepository lightningIssueGroupRepository;

    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private ChatWebSocketService chatWebSocketService;

    @Override
    public ServerResponse handle(TaskCardReturnMessageDTO message, NoticeEntity notice) {
        String errorLogHead = "| - > [ 任务卡片事件处理 ] [ SendUrgeNoticeHandleStrategy ] - ";
        String instanceId = notice.getInstanceId();
        WorkflowInstanceEntity instance = workflowInstanceService.findInstanceById(instanceId);
        if (instance == null) {
            return ServerResponse.ok();
        }
        String taskId = notice.getTaskId();
        if (StringUtils.isBlank(taskId)) {
            String errMsg = "异常，无法获取该通知对应的任务id";
            log.error(errorLogHead + errMsg);
            return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), errMsg);
        }
        // 获取需要催办任务信息
        TaskEntity taskEntity = taskRepository.findById(notice.getTaskId()).orElse(null);
        if (taskEntity == null) {
            String errMsg = "异常，无法获取任务信息";
            log.error(errorLogHead + errMsg);
            return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), errMsg);
        }
        // 催办任务已完成 （不再发送通知）
        if (taskEntity.getStatus() > TaskState.RUNNING.getState()) {
            return ServerResponse.ok();
        }
        // 催办
        try {
            String[] group = message.getTaskId().trim().split(Symbol.UNDERLINE.getValue());
            Integer eventHandleCode = Integer.valueOf(group[1]);
            if (TaskCardHandleEvent.URGE.getCode().equals(eventHandleCode)) {
                workflowInstanceService.urgeTask(taskEntity, InstanceEvent.URGE.getCode());
            } else {
                workflowInstanceService.urgeTask(taskEntity, InstanceEvent.TIME_OUT_BEGIN_URGE.getCode());
            }
            // 获取催办人，问题id
            sendUrgeGroupMessage(instance.getBusinessKey(), message.getFromUserName());
            return ServerResponse.ok();
        } catch (Exception e) {
            return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(), e.getMessage());
        }
    }

    private void sendUrgeGroupMessage(String businessKey, String fromUserName) {
        Integer issueId = Integer.valueOf(businessKey);
        LightningIssueGroupEntity groupInfo = lightningIssueGroupRepository.findByIssueId(issueId);
        Long groupId = Long.valueOf(groupInfo.getGroupId());
        ServerResponse<PassportUserInfoDTO> userInfoResponse = passportFeignClient.getIdByUserId(fromUserName);
        if (userInfoResponse.getCode() == ResponseCode.SUCCESS.getCode()) {
            Integer userId = userInfoResponse.getData().getId();
            chatWebSocketService.sendActionMessage(issueId, groupId, userId, LightningApplyStatus.URGE);
        }
    }
}
