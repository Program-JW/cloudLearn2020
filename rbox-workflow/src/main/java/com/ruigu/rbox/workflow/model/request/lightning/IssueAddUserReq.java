package com.ruigu.rbox.workflow.model.request.lightning;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liqingtian
 * @date 2020/01/20 11:56
 */
@Data
public class IssueAddUserReq {

    /**
     * 问题id
     */
    @NotNull(message = "问题id不能为空")
    private Integer issueId;

    /**
     * 添加人员id
     */
    @NotNull(message = "添加人员id不能为空")
    private Integer userId;
}
