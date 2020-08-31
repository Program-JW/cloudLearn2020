package com.ruigu.rbox.workflow.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 下拉选项
 * @author alan.zhao
 */
@Data
@NoArgsConstructor
public class SelectOption {
    private String label;
    private Object value;

    public SelectOption(String label, Object value) {
        this.label = label;
        this.value = value;
    }
}
