package com.ruigu.rbox.workflow.model.bpmn;

import lombok.Data;

import java.util.List;

/**
 * @author alan.zhao
 */
@Data
public class BusinessField {
    private String key;
    private String label;
    private String type;
    private Integer operation;
    private List<BusinessFieldOption> options;

    @Data
    public static class BusinessFieldOption {
        private String value;
        private String label;
    }
}
