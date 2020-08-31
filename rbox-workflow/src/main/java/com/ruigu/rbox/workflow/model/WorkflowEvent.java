package com.ruigu.rbox.workflow.model;

import lombok.Data;
import org.activiti.engine.delegate.event.ActivitiEventType;

/**
 * 流程事件
 *
 * @author alan.zhao
 */
@Data
public class WorkflowEvent {
    private ActivitiEventType type;
    private String data;
}
