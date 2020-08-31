package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.cloud.kanai.security.UserInfo;
import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.model.entity.TaskSubmitBatchLogEntity;
import com.ruigu.rbox.workflow.model.request.TaskForm;
import com.ruigu.rbox.workflow.repository.TaskSubmitBatchLogRepository;
import com.ruigu.rbox.workflow.service.TaskSubmitBatchService;
import com.ruigu.rbox.workflow.service.WorkflowTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liqingtian
 * @date 2019/10/08 16:57
 */
@Slf4j
@Service
public class TaskSubmitBatchServiceImpl implements TaskSubmitBatchService {

    @Autowired
    private WorkflowTaskService workflowTaskService;

    @Autowired
    private TaskSubmitBatchLogRepository taskSubmitBatchLogRepository;

    @Async
    @Override
    public void batchSubmit(TaskForm taskForm, boolean submit, Long userId, Integer batchLogId) {
        log.debug("=========================== 进入批量" + (submit ? "提交" : "保存") + "方法 =======================");
        // 异步方法 先给当前线程设置当前登陆人
        UserInfo info = new UserInfo();
        info.setUserId(userId.intValue());
        UserHelper.setUserInfo(info);
        List<String> ids = taskForm.getIds();
        List<String> failList = new ArrayList<>();
        List<String> sucList = new ArrayList<>();
        int count = 0;
        for (String id : ids) {
            ++count;
            try {
                taskForm.setId(id);
                workflowTaskService.saveTask(taskForm, submit, true, false, userId);
                sucList.add(id);
            } catch (Exception e) {
                failList.add(id);
                log.error("异常。任务Id:" + id, e);
            }
            updateBatchProgress(batchLogId, sucList.size(), failList.size());
        }
        updateBatchSuccessAndFailIds(batchLogId,
                StringUtils.join(sucList, ","), sucList.size(),
                StringUtils.join(failList, ","), failList.size());
    }

    @Override
    public TaskSubmitBatchLogEntity getBatchProgress(Integer batchLogId) throws Exception {
        return taskSubmitBatchLogRepository.findById(batchLogId).orElseThrow(() -> new VerificationFailedException(400, "所以要查询的批处理Id不存在"));
    }

    @Override
    public Integer insertLog(TaskSubmitBatchLogEntity logEntity) {
        try {
            TaskSubmitBatchLogEntity batchLog = taskSubmitBatchLogRepository.save(logEntity);
            return batchLog.getId();
        } catch (Exception e) {
            log.error("异常。保存批处理日志失败。", e);
            return -1;
        }
    }

    @Override
    public void updateBatchProgress(Integer batchLogId, Integer success, Integer fail) {
        taskSubmitBatchLogRepository.updateBatchSuccessAndFailCount(batchLogId, success, fail);
    }

    @Override
    public void updateBatchSuccessAndFailIds(Integer batchLogId, String successIds, Integer success, String failIds, Integer fail) {
        taskSubmitBatchLogRepository.updateBatchSuccessAndFailIds(batchLogId, successIds, success, failIds, fail);
    }
}
