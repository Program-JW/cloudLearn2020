package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.entity.WsApiLogEntity;

/**
 * @author liqingtian
 * @date 2020/01/11 18:06
 */
public interface WsApiLogService {

    /**
     * 插入日志
     *
     * @param wsApiLogEntity websocket日志
     */
    void insertLog(WsApiLogEntity wsApiLogEntity);
}
