package com.ruigu.rbox.workflow.strategy.context;

import com.ruigu.rbox.workflow.model.dto.ReturnWebSocketMessageDTO;
import com.ruigu.rbox.workflow.model.entity.WsApiLogEntity;
import com.ruigu.rbox.workflow.model.enums.WebSocketMessageHandle;
import com.ruigu.rbox.workflow.strategy.WebSocketMessageHandleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.RegEx;
import javax.annotation.Resource;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2020/01/09 1:28
 */
@Slf4j
@Service
public class WebSocketMessageHandleContext {

    @Resource
    private Map<String, WebSocketMessageHandleStrategy> map;

    public void handleMessage(ReturnWebSocketMessageDTO returnMessage, WsApiLogEntity wsLog) {
        if (returnMessage.getAction() != null) {
            String handle = WebSocketMessageHandle.getValue(returnMessage.getAction());
            if (StringUtils.isNotBlank(handle)) {
                map.get(handle).handle(returnMessage, wsLog);
            }
        }
    }
}
