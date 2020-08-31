package com.ruigu.rbox.workflow.strategy;


import com.ruigu.rbox.workflow.model.dto.ReturnWebSocketMessageDTO;
import com.ruigu.rbox.workflow.model.entity.WsApiLogEntity;

/**
 * @author liqingtian
 * @date 2020/01/09 1:25
 */
public interface WebSocketMessageHandleStrategy {

    /**
     * websocket 消息处理
     *
     * @param returnMessage 接收消息
     * @param wsLog         消息日志
     */
    void handle(ReturnWebSocketMessageDTO returnMessage, WsApiLogEntity wsLog);
}
