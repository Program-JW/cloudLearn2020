package com.ruigu.rbox.workflow.service.stock;

import com.ruigu.rbox.cloud.kanai.model.YesOrNoEnum;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.entity.*;
import com.ruigu.rbox.workflow.model.enums.InstanceEvent;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.repository.TaskRepository;
import com.ruigu.rbox.workflow.service.NoticeLogService;
import com.ruigu.rbox.workflow.service.OperationLogService;
import com.ruigu.rbox.workflow.service.RemoteService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenzhenya
 * @date 2019/11/14 16:00
 */
@Slf4j
@Service("callStockLockTask")
public class CallStockLockTask implements JavaDelegate {

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private NoticeLogService noticeLogService;

    @Autowired
    private TaskRepository taskRepository;

    @Resource
    private RemoteService remoteService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(DelegateExecution delegateExecution) {

        final String APPLY_ID = "businessKey";
        final String NOTICE_EXECUTE_STATUS = "lockNoticeExecuteStatus";

        // 获取申请id
        Map<String, Object> variables = delegateExecution.getVariables();
        Integer applyId = Integer.valueOf(String.valueOf(variables.get(APPLY_ID)));
        // 通知状态
        int noticeExecuteStatus = YesOrNoEnum.NO.getCode();
        // 通知业务系统执行
        try {
            Map<String, Object> param = new HashMap<>(5);

            param.put("applyId", applyId);
            log.info("调用scm节点");
            ServerResponse<Object> serverResponse = remoteService.request("PUT", "http://scm//stock-lock-apply/stock-lock-data-save", param);
            if (serverResponse.getCode() == ResponseCode.SUCCESS.getCode()) {
                // 成功的话 必定会有lockApplyEntity
                Map<String, Object> applyMap = (Map<String, Object>) serverResponse.getData();
                // 记录本次动作
                recordLog(applyMap);
                // 打成功标志位
                noticeExecuteStatus = YesOrNoEnum.YES.getCode();
            }
        } finally {
            log.info(" ------lock notice execute status : {} ------ ", noticeExecuteStatus);
            delegateExecution.setVariable(NOTICE_EXECUTE_STATUS, noticeExecuteStatus);
        }

    }

    private void recordLog(Map<String, Object> applyMap) {
        // 用于标识日志
        final String EVENT = "lockNoticeExecute";

        String instanceId = (String) applyMap.get("instanceId");

        //组装content
        String content = null;
        Integer locked = (Integer) applyMap.get("locked");
        if (locked == YesOrNoEnum.YES.getCode()) {
            // 调用成功
            content = "[ 库存锁定 ] 成功";
        } else {
            // 因为各种原因调用失败
            content = "[ 库存锁定 ] 失败";
        }
        // 防止重复打日志
        List<OperationLogEntity> noticeExecuteLog = operationLogService.getLogByInstanceIdAndEvent(instanceId, EVENT);
        if (CollectionUtils.isNotEmpty(noticeExecuteLog)) {
            return;
        }

        // 兼容老流程（老流程因为没有 noticeExecute 的事件日志）
        // 因此用serviceTask的通知日志来进行防重
        List<NoticeEntity> noticeLog = noticeLogService.getListNoticeByInstanceIdAndType(instanceId, InstanceEvent.SERVER_TASK.getCode());
        if (CollectionUtils.isNotEmpty(noticeLog)) {
            return;
        }

        List<TaskEntity> tasks = taskRepository.findAllByInstanceId(instanceId);
        List<List<String>> taskUserList = tasks.stream()
                .map(t -> Arrays.asList(t.getCandidateUsers().split(",")))
                .collect(Collectors.toList());
        int[] users = taskUserList.stream()
                .flatMapToInt(childList -> childList.stream().mapToInt(Integer::new)).toArray();
        Set<Integer> targets = new HashSet<>(Arrays.asList(ArrayUtils.toObject(users)));
        Integer createdBy = (Integer) applyMap.get("createdBy");
        targets.add(createdBy);

        OperationLogEntity operationLogEntity = new OperationLogEntity();
        NoticeEntity noticeEntity = new NoticeEntity();
        String definitionId = (String) applyMap.get("definitionId");
        Date now=new Date();
        // 通知日志
        noticeEntity.setCreatedOn(now);
        noticeEntity.setContent(content);
        noticeEntity.setTargets(StringUtils.join(targets, ","));
        noticeEntity.setDefinitionId(definitionId);
        noticeEntity.setInstanceId(instanceId);
        noticeEntity.setType(InstanceEvent.SERVER_TASK.getCode());
        noticeEntity.setStatus((byte) -1);
        noticeLogService.insertNotice(noticeEntity);
        // 操作日志
        operationLogEntity.setContent(content);
        operationLogEntity.setDefinitionId(definitionId);
        operationLogEntity.setInstanceId(instanceId);
        operationLogEntity.setCreatedBy(createdBy);
        operationLogEntity.setLastUpdatedBy(createdBy);
        operationLogEntity.setCreatedOn(now);
        operationLogEntity.setLastUpdatedOn(now);
        operationLogEntity.setStatus(1);
        operationLogService.log(operationLogEntity);
    }
}
