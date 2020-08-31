package com.ruigu.rbox.workflow.config;

import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.model.ActionConstants;
import com.ruigu.rbox.workflow.model.client.AbstractReconnectWebSocketClient;
import com.ruigu.rbox.workflow.model.dto.ReturnWebSocketMessageDTO;
import com.ruigu.rbox.workflow.model.entity.WsApiLogEntity;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.repository.WsApiLogRepository;
import com.ruigu.rbox.workflow.strategy.context.WebSocketMessageHandleContext;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author liqingtian
 * @date 2020/01/11 14:28
 */
@Slf4j
@Component
public class ReconnectWebSocketConfig {

    @Resource
    private WsApiLogRepository wsApiLogRepository;

    @Resource
    private WebSocketMessageHandleContext webSocketMessageHandleContext;

    @Bean
    public AbstractReconnectWebSocketClient webSocketClient(@Value("${rbox.chat.websocket.uri}") String chatWebSocketUri,
                                                            @Value("${rbox.chat.websocket.robot}") String robotName) {
        String errorLogHead = "| - > WebSocketClient ";
        log.info(errorLogHead + "  " + chatWebSocketUri);
        try {
            AbstractReconnectWebSocketClient reconnectWebSocketClient = new AbstractReconnectWebSocketClient(new URI(chatWebSocketUri), new Draft_6455()) {
                @Override
                protected void open(ServerHandshake serverHandshake) {
                    log.info("| - > [ WebSocket ] 连接成功 - {} - {}",
                            serverHandshake.getHttpStatus(), serverHandshake.getHttpStatusMessage());
                    try {
                        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
                        service.scheduleAtFixedRate(new Runnable() {
                            @Override
                            public void run() {
                                log.info("| - > [ WebSocket ] - > ping");
                                sendPing();
                            }
                        }, 1L, 30L, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        throw new GlobalRuntimeException(ResponseCode.INTERNAL_ERROR.getCode(), "连接异常");
                    }
                }

                @Override
                protected void message(String message) {
                    messageHandle(message, errorLogHead);
                }

                @Override
                protected void close(int code, String reason, boolean remote) {
                    log.info("| - > [ WebSocket ] 退出连接");
                }

                @Override
                protected void error(Exception ex) {
                    log.info("| - > [ WebSocket ] 连接错误：{}", ex.getMessage());
                }
            };
            reconnectWebSocketClient.connect();
            return reconnectWebSocketClient;
        } catch (Exception e) {
            log.error("| - > [ WebSocket ] 初始化|连接异常：{}", e);
        }
        return null;
    }

    private void messageHandle(String message, String errorLogHead) {
        log.info("| - > [ WebSocket ] 收到消息：{}", message);
        ReturnWebSocketMessageDTO returnMessage = JsonUtil.parseObject(message, ReturnWebSocketMessageDTO.class);
        // 校验返回消息
        if (returnMessage.getAction() == ActionConstants.LOGIN) {
            if (returnMessage.getResult() == ResponseCode.SUCCESS.getCode()) {
                log.info("| - > [ WebSocket ] 机器人登陆成功：{}", returnMessage.getFromConnName());
            }
            return;
        }
        Long messageId = returnMessage.getRandomCode();
        if (messageId == null) {
            log.error(errorLogHead + "数据出错，randomCode查询不到");
            return;
        }
        // 保存返回结果
        WsApiLogEntity wsLog = wsApiLogRepository.findFirstByMessageId(messageId.toString());
        if (wsLog == null) {
            log.error(errorLogHead + "数据异常，根据randomCode无法查询到日志");
            return;
        }
        wsLog.setReturnMessage(message);
        wsApiLogRepository.save(wsLog);
        // 调用策略处理不同message
        webSocketMessageHandleContext.handleMessage(returnMessage, wsLog);
    }
}
