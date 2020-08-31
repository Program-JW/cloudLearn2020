package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.ServerResponse;

import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/09/23 14:52
 */
public interface MpService {

    /**
     *  获取采购单详情
     * @param orderNumber mp采购单单号
     * @param taskId 任务id
     * @return ServerResponse<Map<String, Object>>
     */
    ServerResponse<Map<String, Object>> detail(String orderNumber, String taskId);

    /**
     *  获取采购单流程信息
     * @param orderNumber mp采购单单号
     * @param instanceId 流程id
     * @return ServerResponse<Map<String, Object>>
     */
    ServerResponse instance(String orderNumber, String instanceId);
}
