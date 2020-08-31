package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.entity.TaskSubmitBatchLogEntity;
import com.ruigu.rbox.workflow.model.request.TaskForm;

/**
 * @author liqingtian
 * @date 2019/10/08 16:48
 */
public interface TaskSubmitBatchService {

    /**
     * 批处理提交任务
     *
     * @param userId     操作人ID
     * @param batchLogId 批量记录id
     * @param submit     是否提交
     * @param taskForm   任务参数
     */
    void batchSubmit(TaskForm taskForm, boolean submit, Long userId, Integer batchLogId);

    /**
     * 查询批处理提交进度
     *
     * @param batchLogId 批处理日志id
     * @return 批处理进度信息
     * @throws Exception 不存在
     */
    TaskSubmitBatchLogEntity getBatchProgress(Integer batchLogId) throws Exception;

    /**
     * 插入日志
     *
     * @param logEntity 批处理日志实体
     * @return 日志id
     */
    Integer insertLog(TaskSubmitBatchLogEntity logEntity);

    /**
     * 更新进度
     *
     * @param batchLogId 批处理日志id
     * @param success    成功数
     * @param fail       失败数
     */
    void updateBatchProgress(Integer batchLogId, Integer success, Integer fail);

    /**
     * 更新失败与成功id,同时更新成功失败数
     *
     * @param batchLogId 批处理日志id
     * @param successIds 成功id
     * @param success    成功数
     * @param failIds    失败id
     * @param fail       失败数
     */
    void updateBatchSuccessAndFailIds(Integer batchLogId, String successIds, Integer success, String failIds, Integer fail);
}
