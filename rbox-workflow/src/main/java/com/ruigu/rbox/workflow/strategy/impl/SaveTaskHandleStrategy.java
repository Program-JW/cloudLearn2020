package com.ruigu.rbox.workflow.strategy.impl;

import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.cloud.kanai.util.TimeUtil;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.bpmn.Button;
import com.ruigu.rbox.workflow.model.dto.TaskCardReturnMessageDTO;
import com.ruigu.rbox.workflow.model.entity.NoticeEntity;
import com.ruigu.rbox.workflow.model.entity.NoticeTemplateEntity;
import com.ruigu.rbox.workflow.model.entity.OperationLogEntity;
import com.ruigu.rbox.workflow.model.entity.TaskEntity;
import com.ruigu.rbox.workflow.model.enums.*;
import com.ruigu.rbox.workflow.model.request.TaskForm;
import com.ruigu.rbox.workflow.model.request.TaskFormItem;
import com.ruigu.rbox.workflow.repository.TaskRepository;
import com.ruigu.rbox.workflow.service.NoticeConfigService;
import com.ruigu.rbox.workflow.service.OperationLogService;
import com.ruigu.rbox.workflow.service.WorkflowTaskService;
import com.ruigu.rbox.workflow.strategy.TaskCardHandleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author liqingtian
 * @date 2019/12/31 17:10
 */
@Slf4j
@Component
public class SaveTaskHandleStrategy implements TaskCardHandleStrategy {

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private WorkflowTaskService workflowTaskService;

    @Resource
    private NoticeConfigService noticeConfigService;

    @Resource
    private OperationLogService operationLogService;

    @Override
    public ServerResponse handle(TaskCardReturnMessageDTO message, NoticeEntity notice) {
        String errorLogHead = "| - > [ 任务卡片事件处理 ] [ SaveTaskHandleStrategy ] - ";
        TaskEntity task = taskRepository.selectNoticeContentByTaskId(notice.getTaskId());
        // 查询任务卡片相应button配置
        List<NoticeTemplateEntity> noticeTemplate = noticeConfigService.getNoticeTemplate(NoticeConfigState.NODE,
                task.getNodeId(), InstanceEvent.TASK_CREATE.getCode());
        NoticeTemplateEntity template = noticeTemplate.stream().filter(t -> NoticeType.WEIXIN_CHANNEL.getState() == t.getChannel()
                && NoticeType.TASK_CARD.getState() == t.getType()).findFirst().orElse(null);
        if (template == null) {
            String errMsg = "异常，查询不到具体的消息通知配置";
            log.error(errorLogHead + errMsg);
            return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), errMsg);
        }
        // 记录操作日志
        OperationLogEntity logEntity = new OperationLogEntity();
        logEntity.setTaskId(task.getId());
        logEntity.setInstanceId(task.getInstanceId());
        List<Button> buttons = JsonUtil.parseArray(template.getButtonConfig(), Button.class);
        if (CollectionUtils.isEmpty(buttons)) {
            String errMsg = "异常，查询不到该任务配置的按钮信息";
            log.error(errorLogHead + errMsg);
            return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), errMsg);
        }
        TaskForm taskForm = new TaskForm();
        Button clickBtn = buttons.stream().filter(btn -> message.getEventKey().equals(btn.getKey())).findFirst().orElse(null);
        if (clickBtn != null) {
            TaskFormItem param = new TaskFormItem();
            handlerData(param, clickBtn);
            taskForm.setFormData(Collections.singletonList(param));
            taskForm.setId(task.getId());
        } else {
            String errMsg = "异常，按钮配置中找不到点击按钮的信息";
            log.error(errorLogHead + errMsg);
            return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), errMsg);
        }
        // 提交任务
        try {
            workflowTaskService.saveTask(taskForm, true, false, false, UserHelper.getUserId().longValue());
            logEntity.setContent("[ " + task.getName() + " ]" + clickBtn.getName() + "成功");
            return ServerResponse.ok();
        } catch (Exception e) {
            log.error(errorLogHead + "提交任务失败，失败原因：{}", e);
            return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(), e.getMessage());
        } finally {
            logEntity.setContent("[ " + task.getName() + " ]" + clickBtn.getName() + "失败");
            operationLogService.log(logEntity);
        }
    }

    private void handlerData(TaskFormItem param, Button btn) {
        param.setName(btn.getVarName());
        String value = btn.getVarValue();
        Integer type = btn.getVarType();
        if (type == DataType.OTHER_DATA.getState()) {
            param.setValue(value);
        } else if (type == DataType.STRING_DATA.getState()) {
            param.setValue(value);
        } else if (type == DataType.INTEGER_DATA.getState()) {
            param.setValue(Integer.valueOf(value));
        } else if (type == DataType.DOUBLE_DATA.getState()) {
            param.setValue(Double.valueOf(value));
        } else if (type == DataType.DATE_DATA.getState()) {
            param.setValue(TimeUtil.format(value, TimeUtil.FORMAT_DATE_TIME));
        }
    }
}
