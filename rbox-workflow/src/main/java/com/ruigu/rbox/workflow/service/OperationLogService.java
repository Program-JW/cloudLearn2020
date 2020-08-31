package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.entity.OperationLogEntity;
import com.ruigu.rbox.workflow.model.request.OperationLogRequest;
import com.ruigu.rbox.workflow.model.vo.OperationLogVO;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * @author liqingtian
 * @date 2019/09/02 19:51
 */
public interface OperationLogService {

    /**
     * 记录操作日志
     *
     * @param logEntity 操作日志信息
     */
    @Async
    void log(OperationLogEntity logEntity);

    /**
     * 查询日志列表
     *
     * @param logRequest 日志请求参数
     * @return 日志数据列表分页
     */
    Page<OperationLogVO> getAllLogsPage(OperationLogRequest logRequest);

    /**
     * 获取当前流程id的日志
     *
     * @param instanceId 流程实例id
     * @return 该流程日志记录
     */
    List<OperationLogEntity> getInstanceLog(String instanceId);

    /**
     * 获取某个流程某时间日志
     */
    List<OperationLogEntity> getLogByInstanceIdAndEvent(String instanceId, String event);
}
