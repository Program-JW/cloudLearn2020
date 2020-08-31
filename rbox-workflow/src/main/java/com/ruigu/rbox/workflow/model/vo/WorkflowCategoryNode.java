package com.ruigu.rbox.workflow.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 流程分类节点
 * @author alan.zhao
 */
@Data
public class WorkflowCategoryNode {
    private Integer id;
    private String name;
    private String definitionKey;
    private List<WorkflowCategoryNode> children;
    private boolean hideHidden;
}
