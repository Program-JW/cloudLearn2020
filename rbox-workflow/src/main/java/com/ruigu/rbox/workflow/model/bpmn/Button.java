package com.ruigu.rbox.workflow.model.bpmn;

import lombok.Data;

/**
 * @author alan.zhao
 */
@Data
public class Button {
    private String key;
    private String name;
    private String varName;
    private Integer varType;
    private String varValue;
    private String replaceName;
    private String buttonEvent;
}
