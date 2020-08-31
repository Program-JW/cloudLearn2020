package com.ruigu.rbox.workflow.model.dto;

import lombok.Data;

/**
 * @author caojinghong
 * @date 2020/01/10 17:57
 */
@Data
public class UpdateIssueLogEscalateDTO {
    /**
     * 问题id
     */
    private Integer issueId;
    /**
     * 受理人id
     */
    private Integer resolverId;
    /**
     * 超时等级
     */
    private Integer level;
}
