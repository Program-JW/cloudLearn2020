package com.ruigu.rbox.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.model.entity.ChatWebSocketUser;
import lombok.Data;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/01/08 19:01
 */
@Data
public class SendWebSocketMessageDTO {

    private String param;
    private Integer action;

    public void setContent(MessageContent messageContent) {
        this.param = JsonUtil.toJsonString(messageContent);
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MessageContent {
        private String fromConnName;
        private String fromConnNickName;
        private Long randomCode;
        private Integer appId;
        private Long groupId;
        private String content;
        private List<ChatWebSocketUser> userList;
        private List<ChatWebSocketUser> addUserList;
        private String groupTitle;
        private String robotConnName;
    }
}
