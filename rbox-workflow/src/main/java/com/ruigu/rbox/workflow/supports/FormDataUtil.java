package com.ruigu.rbox.workflow.supports;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruigu.rbox.cloud.kanai.util.TimeUtil;
import com.ruigu.rbox.workflow.model.entity.TaskEntity;
import com.ruigu.rbox.workflow.model.enums.FormContentParam;
import com.ruigu.rbox.workflow.model.vo.WorkflowInstanceVO;
import com.ruigu.rbox.workflow.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author liqingtian
 * @date 2019/09/09 14:54
 */
@Slf4j
@Component
public class FormDataUtil {

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskRepository taskRepository;

    /**
     * 解析并获取（流程定义 - 起始表单 ）结构 Map
     * 解析数据源： WorkForm.formContent
     *
     * @return [{
     * "code":"",
     * "value":"",
     * "rule":"",
     * "":"",
     * "":""
     * }]
     */
    public List<Map<String, Object>> getFormDefinition(JSONArray list) {
        List<Map<String, Object>> formDataList = new ArrayList<>();
        getCodeToMap(list, formDataList);
        return formDataList;
    }

    /**
     * 递归
     */
    private void getCodeToMap(JSONArray list, List<Map<String, Object>> formDataList) {
        if (list == null) {
            return;
        }
        list.forEach(element -> {
            JSONObject elementData = (JSONObject) element;
            if (elementData.getString(FormContentParam.TYPE.getText()).contains(FormContentParam.CONTAINER.getText())) {
                if (elementData.containsKey(FormContentParam.LIST.getText())) {
                    getCodeToMap(elementData.getJSONArray(FormContentParam.LIST.getText()), formDataList);
                }
            } else {
                if (elementData.getJSONObject(FormContentParam.OPTIONS.getText()).containsKey(FormContentParam.CODE.getText())) {
                    Map<String, Object> data = new HashMap<>(16);
                    data.put(FormContentParam.VALUE.getText(), null);
                    data.putAll(elementData.getJSONObject(FormContentParam.OPTIONS.getText()));
                    formDataList.add(data);
                }
            }
        });
    }

    /**
     * 填充Value (起始表单填充)
     */
    public void setFormData(List<Map<String, Object>> formDataList, WorkflowInstanceVO instance) {
        if (instance.getHistory()) {
            historyInstanceSetData(instance.getId(), formDataList);
        } else {
            runInstanceSetData(instance.getId(), formDataList);
        }
    }

    /**
     * 已完成实例填充数据
     */
    private void historyInstanceSetData(String instanceId, List<Map<String, Object>> formDataList) {
        List<HistoricVariableInstance> variables = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(instanceId).list();
        formDataList.forEach(data -> {
            variables.stream()
                    .filter(v -> v.getVariableName().equals(data.get("code")))
                    .findFirst().ifPresent(var -> data.put(FormContentParam.VALUE.getText(), toStringValue(var.getValue())));
        });
    }

    /**
     * 未完成
     */
    private void runInstanceSetData(String instanceId, List<Map<String, Object>> formDataList) {
        // 获取流程变量
        List<TaskEntity> taskCompleteList = taskRepository.selectCompleteTaskByInstanceId(instanceId);
        taskCompleteList.forEach(task -> {
            try {
                List<HistoricVariableInstance> variables = historyService.createHistoricVariableInstanceQuery().taskId(task.getId()).list();
                formDataList.forEach(data -> {
                    variables.stream()
                            .filter(v -> v.getVariableName().equals(data.get(FormContentParam.CODE.getText())))
                            .findFirst().ifPresent(var -> data.put(FormContentParam.VALUE.getText(), toStringValue(var.getValue())));
                });
            } catch (Exception e) {
                log.error("查询任务流程变量异常。" + "任务ID:" + task.getId() + " -- > 报错原因：" + e);
            }
        });
        // 获取保存在未提交任务中的变量数据
        List<TaskEntity> taskIncompleteList = taskRepository.selectIncompleteTaskByInstanceId(instanceId);
        taskIncompleteList.forEach(task -> {
            Map<String, Object> variables = taskService.getVariables(task.getId());
            JSONObject taskData = JSONObject.parseObject(task.getData());
            formDataList.forEach(data -> {
                String code = String.valueOf(data.get(FormContentParam.CODE.getText()));
                if (variables.containsKey(code)) {
                    data.put(FormContentParam.VALUE.getText(), toStringValue(variables.get(code)));
                    if (taskData != null) {
                        if (taskData.containsKey(code)) {
                            data.put(FormContentParam.VALUE.getText(), toStringValue(taskData.get(code)));
                        }
                    }
                }
            });
        });
    }

    public void setFormData(List<Map<String, Object>> formDataList, Map<String, Object> variable) {
        if (variable == null) {
            return;
        }
        formDataList.forEach(form -> {
            String code = String.valueOf(form.get(FormContentParam.CODE.getText()));
            if (variable.containsKey(code)) {
                String value = toStringValue(variable.get(code));
                form.put(FormContentParam.VALUE.getText(), value);
            }
        });
    }

    public Map<String, Object> getDataCode(List<Map<String, Object>> formDataList) {
        Map<String, Object> code = new HashMap<>(16);
        formDataList.forEach(data -> {
            code.put(String.valueOf(data.get(FormContentParam.CODE.getText())), data.get(FormContentParam.VALUE.getText()));
        });
        return code;
    }

    /**
     * 翻译初始表单参数
     */
    public Map<String, Object> translateProcessParam(List<Map<String, Object>> formDataList) {
        Map<String, Object> param = new HashMap<>(16);
        formDataList.forEach(data -> {
            Map<String, Object> formData = (Map<String, Object>) data;
            String code = toStringValue(formData.get(FormContentParam.CODE.getText()));
            String value = toStringValue(formData.get(FormContentParam.VALUE.getText()));
            if (StringUtils.isNotBlank(value) && formData.containsKey(FormContentParam.DATASOURCE.getText())) {
                JSONArray rules = JSONArray.parseArray(String.valueOf(formData.get(FormContentParam.DATASOURCE.getText())));
                rules.forEach(rule -> {
                    JSONObject ruleData = (JSONObject) rule;
                    if (ruleData.containsValue(formData.get(FormContentParam.VALUE.getText()))) {
                        String display = toStringValue(ruleData.getString(FormContentParam.LABEL.getText()));
                        param.put(code, display);
                        param.put(code + "Display", display);
                    }
                });
            } else {
                param.put(code, value);
            }
        });
        return param;
    }

    /**
     * 翻译业务参数
     */
    public Map<String, Object> translateBusinessParam(String ruleString, Map<String, Object> businessParams) {
        Map<String, Object> valueMap = new HashMap<>(16);
        if (StringUtils.isBlank(ruleString)) {
            return valueMap;
        }
        if (businessParams == null || businessParams.isEmpty()) {
            return valueMap;
        }
        List<Map<String, Object>> rules = (List<Map<String, Object>>) JSONArray.parse(ruleString);
        businessParams.keySet().forEach(paramKey -> {
            Map<String, Object> ruleMap = rules.stream()
                    .filter(rule -> String.valueOf(rule.get(FormContentParam.KEY.getText())).equals(paramKey))
                    .findFirst().orElse(null);
            if (ruleMap.containsKey(FormContentParam.OPTIONS.getText())) {
                List<Map<String, Object>> options = (List<Map<String, Object>>) ruleMap.get(FormContentParam.OPTIONS.getText());
                Map<String, Object> optionMap = options.stream()
                        .filter(option -> String.valueOf(option.get(FormContentParam.VALUE.getText())).equals(String.valueOf(businessParams.get(paramKey))))
                        .findFirst().orElse(null);
                valueMap.put(paramKey, optionMap.get(FormContentParam.LABEL.getText()));
            } else {
                valueMap.put(paramKey, String.valueOf(businessParams.get(paramKey)));
            }
        });
        return valueMap;
    }

    /**
     * 获取业务参数的操作规则
     */
    public Map<String, Integer> getParamOperation(String ruleString) {
        Map<String, Integer> operationMap = new HashMap<>(16);
        if (StringUtils.isBlank(ruleString)) {
            return operationMap;
        }
        List<Map<String, Object>> rules = (List<Map<String, Object>>) JSONArray.parse(ruleString);
        rules.forEach(rule -> {
            operationMap.put(String.valueOf(rule.get("key")), Integer.valueOf(String.valueOf(rule.get("operation"))));
        });
        return operationMap;
    }

    private String toStringValue(Object value) {
        if (value instanceof Date) {
            return TimeUtil.format((Date) value, TimeUtil.FORMAT_DATE_TIME);
        } else if (value instanceof LocalDateTime) {
            Date time = TimeUtil.localDateTime2Date((LocalDateTime) value);
            return TimeUtil.format(time, TimeUtil.FORMAT_DATE_TIME);
        } else {
            return String.valueOf(value);
        }
    }
}
