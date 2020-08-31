package com.ruigu.rbox.workflow.model.request;

import lombok.Data;

/**
 * @author alan.zhao
 */
@Data
public class SaveFormRequest {
    private Integer type;
    private String id;
    private String form;
}
