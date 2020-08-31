package com.ruigu.rbox.workflow.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 审批配置节点
 *
 * @author alan.zhao
 */
@Data
public class ApprovalNodeVO {
    private Integer nodeId;
    private String name;
    private Integer type;
    private Integer reviewOrder;
    private UserInfoVO approver;
    private Integer status;
    private LocalDateTime approveTime;
    @JsonIgnore
    private List<Integer> candidateUserIds;
    private List<UserInfoVO> candidateUsers;
}
