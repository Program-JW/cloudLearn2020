package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.workflow.model.entity.TaskSubmitBatchLogEntity;
import com.ruigu.rbox.workflow.model.enums.TaskState;
import com.ruigu.rbox.workflow.model.request.TaskForm;
import com.ruigu.rbox.workflow.repository.TaskRepository;
import com.ruigu.rbox.workflow.service.TaskSubmitBatchService;
import com.ruigu.rbox.workflow.service.WorkflowTaskBatchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liqingtian
 * @date 2019/11/05 14:07
 */

@Service
public class WorkflowTaskBatchServiceImpl implements WorkflowTaskBatchService {

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private TaskSubmitBatchService taskSubmitBatchService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveTaskList(TaskForm taskForm, boolean submit, Long userId) {
        List<String> ids = taskForm.getIds();
        // 修改状态
        taskRepository.updateProcessingStatus(ids, TaskState.RUNNING.getState());
        // 插入批处理记录
        TaskSubmitBatchLogEntity batchLogEntity = new TaskSubmitBatchLogEntity();
        batchLogEntity.setTotal(ids.size());
        batchLogEntity.setCreatedBy(userId.intValue());
        Integer batchLogId = taskSubmitBatchService.insertLog(batchLogEntity);
        // 批处理
        taskSubmitBatchService.batchSubmit(taskForm, submit, userId, batchLogId);
        return batchLogId;
    }
}
