package com.ruigu.rbox.workflow.model.response;

import com.ruigu.rbox.workflow.model.entity.WorkflowDefinitionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author alan.zhao
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WorkflowDefinitionDetail extends WorkflowDefinitionEntity {
    private String graph;
    private String form;
}
