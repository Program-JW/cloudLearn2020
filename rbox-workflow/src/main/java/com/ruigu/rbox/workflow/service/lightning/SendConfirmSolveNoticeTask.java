package com.ruigu.rbox.workflow.service.lightning;

import com.ruigu.rbox.workflow.model.entity.NoticeTemplateEntity;
import com.ruigu.rbox.workflow.model.entity.WorkflowDefinitionEntity;
import com.ruigu.rbox.workflow.model.enums.*;
import com.ruigu.rbox.workflow.model.vo.MessageInfoVO;
import com.ruigu.rbox.workflow.repository.WorkflowDefinitionRepository;
import com.ruigu.rbox.workflow.service.NoticeConfigService;
import com.ruigu.rbox.workflow.strategy.context.SendNoticeContext;
import com.ruigu.rbox.workflow.supports.NoticeContentUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author liqingtian
 * @date 2019/12/31 18:18
 */
@Slf4j
@Service
public class SendConfirmSolveNoticeTask implements JavaDelegate {

    @Resource
    private WorkflowDefinitionRepository workflowDefinitionRepository;

    @Resource
    private SendNoticeContext sendNoticeContext;

    @Resource
    private NoticeConfigService noticeConfigService;

    @Resource
    private NoticeContentUtil noticeContentUtil;

    @Resource
    private RuntimeService runtimeService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        // 该模板已配置到数据库中
        // 查询流程定义信息
        WorkflowDefinitionEntity definition = workflowDefinitionRepository.findByInstanceId(delegateExecution.getProcessInstanceId());
        if (definition == null) {
            log.error("异常，查询不到流程定义信息");
            return;
        }
        Map<String, Object> variables = delegateExecution.getVariables();
        Long creatorId = (Long) variables.get(InstanceVariableParam.INSTANCE_CREATOR_ID.getText());
        String instanceId = delegateExecution.getProcessInstanceId();
        String nodeId = definition.getId() + Symbol.COLON.getValue() + delegateExecution.getCurrentActivityId();
        List<NoticeTemplateEntity> noticeTemplates = noticeConfigService.getNoticeTemplate(NoticeConfigState.NODE,
                nodeId, InstanceEvent.SERVER_TASK.getCode());
        NoticeTemplateEntity template = noticeTemplates.stream()
                .filter(t -> t.getChannel() == NoticeType.WEIXIN_CHANNEL.getState()
                        && t.getType() == NoticeType.TEXT_CARD.getState()).findFirst().orElse(null);
        if (template != null) {
            MessageInfoVO message = noticeContentUtil.translateDefinitionTemplate(template, definition, instanceId, variables);
            message.setTargets(Collections.singleton(creatorId.intValue()));
            message.setNoticeEventType(InstanceEvent.SERVER_TASK.getCode());
            sendNoticeContext.send(template, message);
        }
    }
}
