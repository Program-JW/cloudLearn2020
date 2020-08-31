package com.ruigu.rbox.workflow.model.dto;

import com.ruigu.rbox.workflow.model.entity.ChatWebSocketUser;
import lombok.Data;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/01/08 19:02
 */
@Data
public class ReturnWebSocketMessageDTO {

    private Integer action;
    private Integer result;
    private String fromConnName;
    private String fromConnNickName;
    private Long randomCode;
    private Long msgId;
    private Long groupId;
    private String groupTitle;
    private List<ChatWebSocketUser> addUserList;
    private String robotConnName;
}
