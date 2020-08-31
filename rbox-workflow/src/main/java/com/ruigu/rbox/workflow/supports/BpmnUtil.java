package com.ruigu.rbox.workflow.supports;

import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.model.bpmn.*;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.exceptions.XMLException;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.*;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.util.*;

/**
 * 流程图工具类
 *
 * @author alan.zhao
 */
public class BpmnUtil {

    /**
     * 计算时间表达式为分钟数
     *
     * @param expression 时间表达式 形如 1w 1d 1h 1m
     * @author ：alan.zhao
     * @date ：2019-09-20 23:47:53
     */
    public static Long computeTimeoutAsMinutes(String expression) {
        if (expression == null || expression.trim().length() == 0) {
            return 0L;
        }
        int s = 0;
        int e;
        long time = 0;
        long unit = 0;
        int len = expression.length();
        for (int i = 0; i < len; i++) {
            char c = expression.charAt(i);
            if (c == 'm' || c == 'h' || c == 'd' || c == 'w') {
                e = i;
                int dd = Integer.parseInt(expression.substring(s, e).trim());
                if (c == 'm') {
                    unit = 1;
                } else if (c == 'h') {
                    unit = 60;
                } else if (c == 'd') {
                    unit = 24 * 60;
                } else if (c == 'w') {
                    unit = 7 * 24 * 60;
                }
                time += dd * unit;
                s = e + 1;
            }
        }
        return time;
    }

    public static Map<String, String> parseNodeAttributes(BaseElement baseElement) {
        Map<String, List<ExtensionAttribute>> attributes = baseElement.getAttributes();
        Map<String, String> result = new HashMap<>(16);
        for (Map.Entry<String, List<ExtensionAttribute>> attributeEntry : attributes.entrySet()) {
            List<ExtensionAttribute> values = attributeEntry.getValue();
            if (values != null && values.size() > 0) {
                result.put(attributeEntry.getKey(), values.get(0).getValue().trim());
            }
        }
        return result;
    }

    public static <T> T toObject(ExtensionElement baseElement, String[] fields, Class<T> resultClass) {
        Map<String, List<ExtensionAttribute>> attributes = baseElement.getAttributes();
        T object = Reflections.newInstance(resultClass);
        for (Map.Entry<String, List<ExtensionAttribute>> attributeEntry : attributes.entrySet()) {
            List<ExtensionAttribute> values = attributeEntry.getValue();
            if (values != null && values.size() > 0) {
                Reflections.setFieldValue(object, attributeEntry.getKey(), parseAttributeValue(attributeEntry));
            }
        }
        int two = 2;
        if (fields != null && fields.length > 0) {
            for (int i = 0; i < fields.length; i = i + two) {
                String key1 = fields[i];
                String key2 = fields[i + 1];
                Reflections.setFieldValue(object, key1, toObjects(baseElement, key2, BusinessField.BusinessFieldOption.class));
            }
        }
        return object;
    }

    public static <T> List<T> toObjects(BaseElement baseElement, String field, Class<T> resultClass) {
        List<T> options = null;
        Map<String, List<ExtensionElement>> children = null;
        if (baseElement instanceof ExtensionElement) {
            children = ((ExtensionElement) baseElement).getChildElements();
        } else {
            children = baseElement.getExtensionElements();
        }
        for (Map.Entry<String, List<ExtensionElement>> entry : children.entrySet()) {
            List<ExtensionElement> list = entry.getValue();
            if (list == null || list.size() == 0) {
                continue;
            }
            if (entry.getKey().equalsIgnoreCase(field)) {
                for (ExtensionElement child : list) {
                    if (options == null) {
                        options = new ArrayList<>();
                    }
                    options.add(toObject(child, null, resultClass));
                }
            }
        }
        return options;
    }

    public static Map<String, Object> parseExtensionAttributes(BaseElement baseElement) {
        Map<String, Object> result = new HashMap<>(16);
        Map<String, List<ExtensionElement>> extensionElements = baseElement.getExtensionElements();
        List<BusinessField> fields = null;
        for (Map.Entry<String, List<ExtensionElement>> entry : extensionElements.entrySet()) {
            List<ExtensionElement> list = entry.getValue();
            if (list == null || list.size() == 0) {
                continue;
            }
            if ("businessField".equalsIgnoreCase(entry.getKey())) {
                for (ExtensionElement extensionElement : list) {
                    BusinessField field = toObject(extensionElement, new String[]{"options", "businessFieldOption"}, BusinessField.class);
                    if (fields == null) {
                        fields = new ArrayList<>();
                    }
                    fields.add(field);
                }

            } else if ("businessUrl".equalsIgnoreCase(entry.getKey())) {
                for (ExtensionElement extensionElement : list) {
                    BusinessUrl businessUrl = toObject(extensionElement, null, BusinessUrl.class);
                    result.put(entry.getKey(), businessUrl);
                }
            } else if ("initialUrl".equalsIgnoreCase(entry.getKey())) {
                for (ExtensionElement extensionElement : list) {
                    InitialUrl initialUrl = toObject(extensionElement, null, InitialUrl.class);
                    result.put(entry.getKey(), initialUrl);
                }
            } else {
                for (ExtensionElement extensionElement : list) {
                    Map<String, List<ExtensionAttribute>> attributes = extensionElement.getAttributes();
                    String value = extensionElement.getElementText();
                    if (value == null) {
                        for (Map.Entry<String, List<ExtensionAttribute>> attributeEntry : attributes.entrySet()) {
                            if ("value".equalsIgnoreCase(attributeEntry.getKey())) {
                                List<ExtensionAttribute> values = attributeEntry.getValue();
                                if (values != null && values.size() > 0) {
                                    value = values.get(0).getValue();
                                    break;
                                }
                            }
                        }
                    }
                    result.put(extensionElement.getName(), value);
                }
            }
        }
        if (fields != null && fields.size() > 0) {
            result.put("businessField", fields);
        }
        return result;
    }

    public static Object parseAttributeValue(Map.Entry<String, List<ExtensionAttribute>> attributeEntry) {
        String value = null;
        List<ExtensionAttribute> values = attributeEntry.getValue();
        if (values != null && values.size() > 0) {
            value = values.get(0).getValue();
        }
        return value;
    }

    public static List<Map<String, Object>> parseBusinessFields(BaseElement baseElement) {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, List<ExtensionElement>> extensionElements = baseElement.getExtensionElements();
        for (Map.Entry<String, List<ExtensionElement>> entry : extensionElements.entrySet()) {
            List<ExtensionElement> list = entry.getValue();
            if ("businessField".equalsIgnoreCase(entry.getKey()) && list != null && list.size() > 0) {
                Map<String, Object> field = new HashMap<>(16);
                for (ExtensionElement extensionElement : list) {
                    Map<String, List<ExtensionAttribute>> attributes = extensionElement.getAttributes();
                    for (Map.Entry<String, List<ExtensionAttribute>> attributeEntry : attributes.entrySet()) {
                        field.put(attributeEntry.getKey(), parseAttributeValue(attributeEntry));
                    }
                    result.add(field);
                }
            }
        }
        return result;
    }

    public static List<Button> parseUserTaskButtons(UserTask userTask) {
        return toObjects(userTask, "button", Button.class);
    }

    public static String parseUserTaskSummary(UserTask userTask) {
        List<TaskSummary> summaryList = toObjects(userTask, "summary", TaskSummary.class);
        if (summaryList == null || summaryList.isEmpty()) {
            return null;
        } else {
            return summaryList.get(0).getValue();
        }
    }

    public static <T> T addDefaultToBpmnString(String definitionCode, String bpmnString, Class<T> resultClass) throws Exception {
        T result = null;
        if (StringUtils.isNotBlank(bpmnString)) {
            BpmnXMLConverter converter = new BpmnXMLConverter();
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(bpmnString));
            BpmnModel bpmnModel = converter.convertToBpmnModel((XMLStreamReader) reader);
            List<Process> ps = bpmnModel.getProcesses();
            if (ps != null && ps.size() > 0) {
                for (Process process : ps) {
                    process.setId(definitionCode);
                    // 设置流程结束监听器
                    boolean hasExecutionListenerEndImpl = false;
                    List<ActivitiListener> executionListeners = process.getExecutionListeners();
                    if (executionListeners == null) {
                        executionListeners = new ArrayList<>();
                    }
                    for (ActivitiListener listener : executionListeners) {
                        if ("delegateExpression".equalsIgnoreCase(listener.getImplementationType()) && listener.getEvent().equalsIgnoreCase(ExecutionListener.EVENTNAME_END) && "${executionEndListenerImpl}".equalsIgnoreCase(listener.getImplementation())) {
                            hasExecutionListenerEndImpl = true;
                            break;
                        }
                    }
                    if (!hasExecutionListenerEndImpl) {
                        ActivitiListener executionListener = new ActivitiListener();
                        executionListener.setEvent(ExecutionListener.EVENTNAME_END);
                        executionListener.setImplementationType("delegateExpression");
                        executionListener.setImplementation("${executionEndListenerImpl}");
                        executionListeners.add(executionListener);
                    }
                    process.setExecutionListeners(executionListeners);

                    Collection<FlowElement> elements = process.getFlowElements();
                    if (elements != null && elements.size() > 0) {
                        for (FlowElement el : elements) {
                            fixFailedJobRetryTimeCycle(el);
                            addDefaultListeners(el);
                        }
                    }
                }
            }
            if (resultClass == String.class) {
                result = (T) new String(converter.convertToXML(bpmnModel, "UTF-8"));
            } else {
                result = (T) bpmnModel;
            }
        }
        return result;
    }

    /**
     * 修复ServiceTask failedJobRetryTimeCycle被扩展元素集合中移到ServiceTask下的问题
     *
     * @param el
     * @throws Exception
     */
    private static void fixFailedJobRetryTimeCycle(FlowElement el) {
        if (el instanceof ServiceTask) {
            ServiceTask serviceTask = (ServiceTask) el;
            if (StringUtils.isNotBlank(serviceTask.getFailedJobRetryTimeCycleValue())) {
                Map<String, List<ExtensionElement>> children = serviceTask.getExtensionElements();
                boolean failedJobRetryTimeCycleExists = false;
                for (Map.Entry<String, List<ExtensionElement>> entry : children.entrySet()) {
                    if (entry.getKey().equalsIgnoreCase("failedJobRetryTimeCycle")) {
                        failedJobRetryTimeCycleExists = true;
                        break;
                    }
                }
                if (!failedJobRetryTimeCycleExists) {
                    ExtensionElement extensionElement = new ExtensionElement();
                    extensionElement.setName("failedJobRetryTimeCycle");
                    extensionElement.setNamespacePrefix("activiti");
                    extensionElement.setNamespace("http://activiti.org/bpmn");
                    extensionElement.setElementText(serviceTask.getFailedJobRetryTimeCycleValue());
                    serviceTask.setFailedJobRetryTimeCycleValue(null);
                    serviceTask.addExtensionElement(extensionElement);
                }
            }
        }
    }

    /**
     * 初始化UserTask默认监听器
     * @param el
     */
    public static void addDefaultListeners(FlowElement el) {
        if (el instanceof UserTask) {
            UserTask ut = (UserTask) el;
            List<ActivitiListener> listeners = ut.getTaskListeners();
            if (listeners == null || listeners.size() == 0) {
                listeners = new ArrayList<>();
            }
            boolean hasAddTaskListenerImpl = false;
            boolean hasCompleteListenerImpl = false;
            boolean hasDeleteTaskListenerImpl = false;
            for (ActivitiListener listener : listeners) {
                if ("delegateExpression".equalsIgnoreCase(listener.getImplementationType()) && TaskListener.EVENTNAME_CREATE.equalsIgnoreCase(listener.getEvent())) {
                    hasAddTaskListenerImpl = true;
                }
                if ("delegateExpression".equalsIgnoreCase(listener.getImplementationType()) && TaskListener.EVENTNAME_DELETE.equalsIgnoreCase(listener.getEvent())) {
                    hasDeleteTaskListenerImpl = true;
                }
                if ("delegateExpression".equalsIgnoreCase(listener.getImplementationType()) && TaskListener.EVENTNAME_COMPLETE.equalsIgnoreCase(listener.getEvent())) {
                    hasCompleteListenerImpl = true;
                }
            }
            if (!hasAddTaskListenerImpl) {
                ActivitiListener taskListener = new ActivitiListener();
                taskListener.setEvent(TaskListener.EVENTNAME_CREATE);
                taskListener.setImplementationType("delegateExpression");
                taskListener.setImplementation("${taskCreateListenerImpl}");
                listeners.add(taskListener);
            }

            if (!hasCompleteListenerImpl) {
                ActivitiListener taskListener = new ActivitiListener();
                taskListener.setEvent(TaskListener.EVENTNAME_COMPLETE);
                taskListener.setImplementationType("delegateExpression");
                taskListener.setImplementation("${taskCompleteListenerImpl}");
                listeners.add(taskListener);
            }
            if (!hasDeleteTaskListenerImpl) {
                ActivitiListener taskListener = new ActivitiListener();
                taskListener.setEvent(TaskListener.EVENTNAME_DELETE);
                taskListener.setImplementationType("delegateExpression");
                taskListener.setImplementation("${taskDeleteListenerImpl}");
                listeners.add(taskListener);
            }
            ut.setTaskListeners(listeners);
        }
    }

    public static BpmnModel toBpmnModel(String bpmnString) throws Exception {
        BpmnModel bpmnModel = null;
        if (StringUtils.isNotBlank(bpmnString)) {
            try {
                BpmnXMLConverter converter = new BpmnXMLConverter();
                XMLInputFactory factory = XMLInputFactory.newInstance();
                XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(bpmnString));
                bpmnModel = converter.convertToBpmnModel((XMLStreamReader) reader);
            } catch (XMLException e) {
                throw new VerificationFailedException(e, 400, bpmnString + "不符合BPMN规范");
            }
        }
        return bpmnModel;
    }
}
