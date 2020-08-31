package com.ruigu.rbox.workflow.supports;

import com.ruigu.rbox.workflow.model.ActionConstants;
import com.ruigu.rbox.workflow.model.dto.RobotAndWebSocketMessageDTO;
import com.ruigu.rbox.workflow.model.dto.SendWebSocketMessageDTO;

import java.util.UUID;

/**
 * @author liqingtian
 * @date 2020/01/13 11:55
 */
public class WebSocketClientUtil {

    public static RobotAndWebSocketMessageDTO getRobotLoginMessage(String robotName) {
        SendWebSocketMessageDTO message = new SendWebSocketMessageDTO();
        SendWebSocketMessageDTO.MessageContent content = new SendWebSocketMessageDTO.MessageContent();
        content.setFromConnName(robotName);
        message.setContent(content);
        message.setAction(ActionConstants.LOGIN);
        RobotAndWebSocketMessageDTO returnInfo = new RobotAndWebSocketMessageDTO();
        returnInfo.setLoginRobotName(robotName);
        returnInfo.setSendWebSocketMessageDTO(message);
        return returnInfo;
    }

    public static SendWebSocketMessageDTO.MessageContent buildRobotMessage(String robotName) {
        SendWebSocketMessageDTO.MessageContent content = new SendWebSocketMessageDTO.MessageContent();
        content.setFromConnName(robotName);
        content.setFromConnNickName(robotName);
        content.setAppId(0);
        return content;
    }
}
