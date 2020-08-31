package com.ruigu.rbox.workflow.model.dto;

import lombok.Data;

/**
 * @author liqingtian
 * @date 2020/01/13 12:01
 */
@Data
public class RobotAndWebSocketMessageDTO {

    private String loginRobotName;

    private SendWebSocketMessageDTO sendWebSocketMessageDTO;
}
