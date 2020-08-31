package com.ruigu.rbox.workflow.strategy;

import com.ruigu.rbox.workflow.model.dto.MsgNotifyDTO;

/**
 * @author liqingtian
 * @date 2020/02/04 9:48
 */
public interface ChatMqMsgHandleStrategy {

    /**
     * 聊天室 rabbit mq 消息处理
     *
     * @param msgNotifyDTO 消息体
     */
    void handle(MsgNotifyDTO msgNotifyDTO);
}
