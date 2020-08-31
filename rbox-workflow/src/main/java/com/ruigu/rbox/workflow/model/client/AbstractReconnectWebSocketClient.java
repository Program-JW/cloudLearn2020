package com.ruigu.rbox.workflow.model.client;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;
import java.util.Timer;

/**
 * @author liqingtian
 * @date 2020/01/10 13:34
 */
@Slf4j
public abstract class AbstractReconnectWebSocketClient extends WebSocketClient {

    private boolean debug = true;

    /**
     * 重连间隔
     */
    private Integer reconnectInterval = 10000;

    /**
     * 最大间隔
     */
    private Integer maxReconnectInterval = 30000;

    /**
     * 衰减
     */
    private Double reconnectDecay = 1.5;

    /**
     * 重连尝试次数
     */
    private Integer reconnectAttempts = 0;

    /**
     * 最大尝试次数5000
     */
    private Integer maxReconnectAttempts = 5000;

    /**
     * 强制关闭
     */
    private Boolean forcedClose = false;

    /**
     * 重连定时器
     */
    private Timer reconnectTimer;

    /**
     * 是否重连
     */
    private Boolean isReconnecting = false;

    /**
     * 重连定时任务
     */
    private AbstractReschedulableTimerTask reconnectTimerTask;


    public AbstractReconnectWebSocketClient(URI serverUri, Draft protocolDraft) {
        super(serverUri, protocolDraft, null, 0);
    }

    public AbstractReconnectWebSocketClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);
    }

    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        log.error("关闭原因：{}", arg1);
        if (forcedClose) {
            // 调用close 方法
            close(arg0, arg1, arg2);
        } else {
            if (!isReconnecting) {
                restartReconnectionTimer();
            }
            isReconnecting = true;
        }
    }

    @Override
    public void onError(Exception exception) {
        error(exception);
    }

    @Override
    public void onMessage(String message) {
        message(message);
    }

    @Override
    public void onOpen(ServerHandshake arg0) {
        open(arg0);
    }

    /**
     * 重启重连定时器
     */
    private void restartReconnectionTimer() {
        // 取消当前定时
        cancelReconnectionTimer();
        // 创建新的定时器及定时任务
        reconnectTimer = new Timer("reconnectTimer");
        reconnectTimerTask = new AbstractReschedulableTimerTask() {
            @Override
            public void run() {
                if (reconnectAttempts >= maxReconnectAttempts) {
                    cancelReconnectionTimer();
                    if (debug) {
                        log.error("以达到最大重试次数:" + maxReconnectAttempts + "，已停止重试!!!!");
                    }
                }
                reconnectAttempts++;
                try {
                    Boolean isOpen = reconnectBlocking();
                    if (isOpen) {
                        if (debug) {
                            log.info("连接成功，重试次数为:" + reconnectAttempts);
                        }
                        cancelReconnectionTimer();
                        reconnectAttempts = 0;
                        isReconnecting = false;
                    } else {
                        if (debug) {
                            log.error("连接失败，重试次数为:" + reconnectAttempts);
                        }
                        long timeout = maxReconnectInterval;
                        log.info("timeout - > {}", timeout);
                        reconnectTimerTask.reSchedule2(timeout);
                    }
                } catch (InterruptedException e) {
                    log.error("| - > [ WebSocket ] 重试异常：{}", e);
                }
            }
        };
        reconnectTimerTask.schedule(reconnectTimer, reconnectInterval);
    }

    /**
     * 取消重连定时器
     */
    private void cancelReconnectionTimer() {
        if (reconnectTimer != null) {
            reconnectTimer.cancel();
            reconnectTimer = null;
        }
        if (reconnectTimerTask != null) {
            reconnectTimerTask.cancel();
            reconnectTimerTask = null;
        }
    }

    /**
     * 连接成功监听
     *
     * @param handshakedata h
     */
    protected abstract void open(ServerHandshake handshakedata);

    /**
     * 接收消息监听
     *
     * @param message 消息
     */
    protected abstract void message(String message);

    /**
     * 连接关闭监听
     *
     * @param code   状态码
     * @param reason 原因
     * @param remote r
     */
    protected abstract void close(int code, String reason, boolean remote);

    /**
     * 连接错误监听
     *
     * @param ex 异常
     */
    protected abstract void error(Exception ex);
}

