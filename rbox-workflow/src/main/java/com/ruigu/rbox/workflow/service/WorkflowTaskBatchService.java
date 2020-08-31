package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.request.TaskForm;

/**
 * @author liqingtian
 * @date 2019/11/05 14:07
 */
public interface WorkflowTaskBatchService {

    /**
     * 保存或提交 (批量)
     *
     * @param taskForm 参数体
     * @param submit   是否是提交 false为暂存 true为提交
     * @param userId   操作人ID
     * @return 失败任务ID
     * @throws Exception 验证失败时抛出Exception
     */
    Integer saveTaskList(TaskForm taskForm, boolean submit, Long userId) throws Exception;
}
