package com.ruigu.rbox.workflow.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author liqingtian
 * @date 2020/01/03 19:29
 */

@Data
public class LightningMyAcceptanceVO {

    /**
     * 问题id
     */
    private Integer issueId;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 群id
     */
    private String groupId;

    /**
     * 群名称
     */
    private String groupName;

    /**
     * 描述
     */
    private String description;

    /**
     * 申请人id
     */
    private Integer createdBy;

    /**
     * 申请人姓名
     */
    private String creatorName;

    /**
     * 申请人头像
     */
    private String headUrl;

    /**
     * 当前受理人id
     */
    private Integer currentSolverId;

    /**
     * 当前受理人姓名
     */
    private String currentSolverName;

    /**
     * 状态
     */
    private Integer status;
    /**
     * 是否系统自动确认
     */
    private Integer autoConfirm;

    /**
     * 最新更新时间
     */
    private Date lastUpdatedOn;
}
