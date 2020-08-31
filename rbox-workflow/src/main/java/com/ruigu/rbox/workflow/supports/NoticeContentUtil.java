package com.ruigu.rbox.workflow.supports;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruigu.rbox.cloud.kanai.util.TimeUtil;
import com.ruigu.rbox.workflow.manager.PassportFeignManager;
import com.ruigu.rbox.workflow.model.dto.BaseTaskInfoDTO;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.entity.*;
import com.ruigu.rbox.workflow.model.enums.*;
import com.ruigu.rbox.workflow.model.vo.MessageInfoVO;
import com.ruigu.rbox.workflow.repository.WorkflowFormRepository;
import com.ruigu.rbox.workflow.service.WorkflowTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author liqingtian
 * @date 2019/10/15 13:56
 */
@Slf4j
@Component
public class NoticeContentUtil {

    @Resource
    private WorkflowFormRepository workflowFormRepository;

    @Resource
    private PassportFeignManager passportFeignManager;

    @Resource
    private WorkflowTaskService taskService;

    @Resource
    private FormDataUtil formDataUtil;

    public MessageInfoVO translateNodeTemplate(NoticeTemplateEntity template,
                                               WorkflowDefinitionEntity definition,
                                               BaseTaskInfoDTO task,
                                               Map<String, Object> variables) {
        if (template == null) {
            return null;
        }
        Map<String, Object> paramMap = returnNodeTemplateParamValue(task, variables);
        // 详情url
        String detailUrlTemplate = StringUtils.isNotBlank(template.getDetailUrl()) ? template.getDetailUrl().trim() : definition.getMobileBusinessUrl();
        paramMap.put(InstanceVariableParam.INITIAL_CODE.getText(), definition.getInitialCode());
        String detailUrl = ElUtil.fill(paramMap, detailUrlTemplate);
        paramMap.put(InstanceVariableParam.TASK_DETAIL_URL.getText(), detailUrl);
        // 转换
        MessageInfoVO messageInfo = setMessageInfo(paramMap, template);
        messageInfo.setTaskId(task.getId());
        return messageInfo;
    }

    public Map<String, Object> returnNodeTemplateParamValue(BaseTaskInfoDTO task,
                                                            Map<String, Object> variables) {
        // 需要给出的参数
        Map<String, Object> paramMap = new HashMap<>(64);
        if (task == null || variables == null || variables.isEmpty()) {
            return paramMap;
        }
        // 获取流程变量
        variables.put(InstanceVariableParam.INSTANCE_ID.getText(), task.getInstanceId());
        Map<String, Object> processVariables = getProcessVariables(variables);
        // 设置流程参数
        paramMap.putAll(processVariables);
        // task信息
        paramMap.put(InstanceVariableParam.TASK_ID.getText(), task.getId());
        paramMap.put(InstanceVariableParam.TASK_NAME.getText(), task.getName());
        return paramMap;
    }

    public MessageInfoVO translateDefinitionTemplate(NoticeTemplateEntity template,
                                                     WorkflowDefinitionEntity definition,
                                                     String instanceId,
                                                     Map<String, Object> variables) {
        if (template == null) {
            return null;
        }
        Map<String, Object> paramMap = returnDefinitionTemplateParamValue(instanceId, variables);
        // 详情url
        String instanceUrlTemplate = StringUtils.isNotBlank(template.getDetailUrl()) ? template.getDetailUrl().trim() : definition.getMobileInitialUrl();
        paramMap.put(InstanceVariableParam.INITIAL_CODE.getText(), definition.getInitialCode());
        String instanceUrl = ElUtil.fill(paramMap, instanceUrlTemplate);
        paramMap.put(InstanceVariableParam.INSTANCE_DETAIL_URL.getText(), instanceUrl);
        // 转换
        return setMessageInfo(paramMap, template);
    }

    public Map<String, Object> returnDefinitionTemplateParamValue(String instanceId,
                                                                  Map<String, Object> variables) {
        variables.put(InstanceVariableParam.INSTANCE_ID.getText(), instanceId);
        // 获取流程变量
        Map<String, Object> processVariables = getProcessVariables(variables);
        // 需要给出的参数
        Map<String, Object> paramMap = new HashMap<>(64);
        // 设置流程参数
        paramMap.putAll(processVariables);
        return paramMap;
    }

    private MessageInfoVO setMessageInfo(Map<String, Object> paramMap, NoticeTemplateEntity template) {
        MessageInfoVO message = new MessageInfoVO();
        message.setTitle(ElUtil.fill(paramMap, template.getTitle()));
        message.setDescription(ElUtil.fill(paramMap, template.getContent()));
        message.setUrl((String) paramMap.get(NoticeParam.URL.getDesc()));
        message.setDefinitionId((String) paramMap.get(InstanceVariableParam.DEFINITION_ID.getText()));
        message.setInstanceId((String) paramMap.get(InstanceVariableParam.INSTANCE_ID.getText()));
        message.setButtonConfig(template.getButtonConfig());
        return message;
    }

    private Map<String, Object> getProcessVariables(Map<String, Object> variables) {
        if (variables == null) {
            return new HashMap<>(4);
        }
        String definitionId = (String) variables.get(InstanceVariableParam.DEFINITION_ID.getText());
        String definitionName = (String) variables.get(InstanceVariableParam.DEFINITION_NAME.getText());
        String instanceId = (String) variables.get(InstanceVariableParam.INSTANCE_ID.getText());
        String instanceName = (String) variables.get(InstanceVariableParam.INSTANCE_NAME.getText());
        Date instanceCreateTime = (Date) variables.get(InstanceVariableParam.INSTANCE_CREATE_TIME.getText());
        Long instanceCreatorId = (Long) variables.get(InstanceVariableParam.INSTANCE_CREATOR_ID.getText());

        // 流程定义的业务参数
        Map<String, Object> processParamMap = new HashMap<>(32);
        processParamMap.putAll(variables);

        // 扩展业务参数
        WorkflowFormEntity form = workflowFormRepository.findByDefinitionId(definitionId);
        if (form != null && StringUtils.isNotBlank(form.getSelectFormContent())) {
            Map<String, Object> businessParams = new HashMap<>(16);
            if (variables.containsKey(InstanceVariableParam.BUSINESS_PARAM.getText())) {
                businessParams = JSONObject.parseObject(String.valueOf(variables.get(InstanceVariableParam.BUSINESS_PARAM.getText())));
            }
            processParamMap.putAll(formDataUtil.translateBusinessParam(form.getSelectFormContent(), businessParams));
        }

        // 转义起始表单
        if (form != null && StringUtils.isNotBlank(form.getFormContent())) {
            JSONObject formContent = JSONObject.parseObject(form.getFormContent());
            JSONArray list = formContent.getJSONArray(FormContentParam.LIST.getText());
            List<Map<String, Object>> formDataList = formDataUtil.getFormDefinition(list);
            formDataUtil.setFormData(formDataList, variables);
            Map<String, Object> processValueMap = formDataUtil.translateProcessParam(formDataList);
            processParamMap.putAll(processValueMap);
        }

        // 各类任务
        List<TaskEntity> tasks = taskService.getAllTaskEntityByInstanceId(instanceId);
        if (CollectionUtils.isNotEmpty(tasks)) {
            tasks.forEach(taskEntity -> {
                if (StringUtils.isNotBlank(taskEntity.getFormContent())) {
                    JSONObject formContent = JSONObject.parseObject(taskEntity.getFormContent());
                    JSONArray list = formContent.getJSONArray(FormContentParam.LIST.getText());
                    List<Map<String, Object>> formDataList = formDataUtil.getFormDefinition(list);
                    formDataUtil.setFormData(formDataList, JSONObject.parseObject(taskEntity.getData()));
                    Map<String, Object> taskValueMap = formDataUtil.translateProcessParam(formDataList);
                    processParamMap.putAll(taskValueMap);
                } else if (taskEntity.getApprovalNode() == 1) {
                    JSONObject approvalValue = JSONObject.parseObject(taskEntity.getData());
                    if (approvalValue != null) {
                        approvalValue.keySet().forEach(key -> {
                            if (TaskSubmitState.PASS.getCode() == Integer.parseInt(approvalValue.getString(key))) {
                                processParamMap.put(key, TaskSubmitState.PASS.getText());
                            } else {
                                processParamMap.put(key, TaskSubmitState.REJECT.getText());
                            }
                        });
                    }
                }
            });
        }

        List<Integer> userIds = new ArrayList<>();

        // 闪电链需要交接人
        Integer receiverId = (Integer) variables.get(InstanceVariableParam.RECEIVER.getText());
        boolean haveReceiver = false;
        if (haveReceiver = receiverId != null) {
            userIds.add(receiverId);
        } else {
            processParamMap.put(InstanceVariableParam.RECEIVER_NAME.getText(), "无");
        }
        userIds.add(instanceCreatorId.intValue());

        Map<Integer, PassportUserInfoDTO> userInfoMap = passportFeignManager.getUserInfoMapFromRedis(userIds);
        if (haveReceiver) {
            PassportUserInfoDTO receiverUserInfo = userInfoMap.get(receiverId);
            if (receiverUserInfo != null) {
                processParamMap.put(InstanceVariableParam.RECEIVER_NAME.getText(), receiverUserInfo.getNickname());
            }
        }
        PassportUserInfoDTO instanceCreatedByUserInfo = userInfoMap.get(instanceCreatorId.intValue());
        String instanceCreatorName = "";
        if (instanceCreatedByUserInfo != null) {
            instanceCreatorName = instanceCreatedByUserInfo.getNickname();
        }

        // 回车(换行符)
        processParamMap.put(InstanceVariableParam.LINE_FEED.getText(), "\n");
        // 实例
        processParamMap.put(InstanceVariableParam.INSTANCE_ID.getText(), instanceId);
        processParamMap.put(InstanceVariableParam.INSTANCE_CREATOR_ID.getText(), instanceCreatorId);
        processParamMap.put(InstanceVariableParam.INSTANCE_CREATOR_NAME.getText(), instanceCreatorName);
        processParamMap.put(InstanceVariableParam.INSTANCE_NAME.getText(), instanceName);
        processParamMap.put(InstanceVariableParam.INSTANCE_CREATE_TIME.getText(), TimeUtil.format(instanceCreateTime, TimeUtil.FORMAT_DATE_TIME));
        // 定义
        processParamMap.put(InstanceVariableParam.DEFINITION_NAME.getText(), definitionName);
        processParamMap.put(InstanceVariableParam.DEFINITION_ID.getText(), definitionId);
        return processParamMap;
    }
}
