package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

/**
 * 聊天专用用户信息
 *
 * @author liqingtian
 * @date 2020/01/08 19:17
 */
@Data
public class ChatWebSocketUser {
    private String connName;
    private String nickName;
}
