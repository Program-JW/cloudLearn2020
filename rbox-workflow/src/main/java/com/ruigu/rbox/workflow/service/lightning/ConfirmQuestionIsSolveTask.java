package com.ruigu.rbox.workflow.service.lightning;

import com.ruigu.rbox.workflow.model.entity.OperationLogEntity;
import com.ruigu.rbox.workflow.model.enums.InstanceEvent;
import com.ruigu.rbox.workflow.model.enums.InstanceVariableParam;
import com.ruigu.rbox.workflow.model.enums.LightningApplyStatus;
import com.ruigu.rbox.workflow.model.enums.WorkflowStatusFlag;
import com.ruigu.rbox.workflow.repository.WorkflowDefinitionRepository;
import com.ruigu.rbox.workflow.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * 确认问题是否已处理 serverTask
 *
 * @author liqingtian
 * @date 2019/12/25 16:03
 */
@Slf4j
@Service
public class ConfirmQuestionIsSolveTask implements JavaDelegate {

    @Resource
    private OperationLogService operationLogService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
//        log.error("===============================  信号接收后触发  ============================");
//        OperationLogEntity logEntity = new OperationLogEntity();
//        logEntity.setEvent(InstanceEvent.SERVER_TASK.getCode().toString());
//        Map<String, Object> variables = delegateExecution.getVariables();
//        Integer status = (Integer) variables.get(WorkflowStatusFlag.TASK_STATUS.getName());
//        logEntity.setShowStatus(status);
//        // 查询流程定义信息
//        logEntity.setTaskId(InstanceEvent.SERVER_TASK.getCode().toString());
//        String instanceId = delegateExecution.getProcessInstanceId();
//        logEntity.setInstanceId(instanceId);
//        String definitionId = (String) variables.get(InstanceVariableParam.DEFINITION_ID.getText());
//        String definitionName = (String) variables.get(InstanceVariableParam.DEFINITION_NAME.getText());
//        logEntity.setDefinitionId(definitionId);
//        logEntity.setContent("[ " + definitionName + " ] [ 申请人确认 ] " + LightningApplyStatus.getDesc(status));
//        logEntity.setCreatedOn(new Date());
//        Long instanceCreatorId = (Long) variables.get(InstanceVariableParam.INSTANCE_CREATOR_ID.getText());
//        logEntity.setCreatedBy(instanceCreatorId.intValue());
//        operationLogService.log(logEntity);
    }
}
