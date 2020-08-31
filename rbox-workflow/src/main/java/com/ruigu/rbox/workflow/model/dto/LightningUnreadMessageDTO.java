package com.ruigu.rbox.workflow.model.dto;

import com.ruigu.rbox.workflow.model.entity.LightningIssueApplyEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/02/03 18:10
 */
@Data
public class LightningUnreadMessageDTO {
    private Long groupId;
    private String groupName;
    private String content;
    private Integer fromUser;
    private String fromUserName;
    private List<Integer> toUserList;
    private LocalDateTime sendTime;
    private LightningIssueApplyEntity issueInfo;
    private String currentUserName;
}
