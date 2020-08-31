package com.ruigu.rbox.workflow.strategy.impl;

import com.ruigu.rbox.workflow.model.LightningReturnMessageInfoMap;
import com.ruigu.rbox.workflow.model.dto.ReturnWebSocketMessageDTO;
import com.ruigu.rbox.workflow.model.entity.WsApiLogEntity;
import com.ruigu.rbox.workflow.strategy.WebSocketMessageHandleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author liqingtian
 * @date 2020/01/09 1:30
 */
@Slf4j
@Service
public class AddUserToGroupHandleStrategy implements WebSocketMessageHandleStrategy {

    @Resource
    private LightningReturnMessageInfoMap lightningReturnMessageInfoMap;

    @Override
    public void handle(ReturnWebSocketMessageDTO returnMessage, WsApiLogEntity wsLog) {
        lightningReturnMessageInfoMap.addMessage(returnMessage);
    }
}
