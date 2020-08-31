package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2019/12/25 18:20
 */
@Getter
@AllArgsConstructor
public enum WorkflowStatusFlag {

    /***
     * 处理结果
     */
    HANDLE_RESULT("handleResult"),

    /**
     * 任务状态
     */
    TASK_STATUS("taskStatus"),

    /**
     * 是否解决
     */
    SOLVE("solve");

    private String name;

}
