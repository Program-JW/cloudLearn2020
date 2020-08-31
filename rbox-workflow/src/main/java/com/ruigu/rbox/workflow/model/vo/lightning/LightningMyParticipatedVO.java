package com.ruigu.rbox.workflow.model.vo.lightning;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author caojinghong
 * @date 2020/02/24 14:11
 */
@ApiModel(value = "我参与的问题列表返回实体")
@Data
public class LightningMyParticipatedVO {
    /**
     * 问题id
     */
    @ApiModelProperty(value = "问题id",name = "issueId")
    private Integer issueId;

    /**
     * 群id
     */
    @ApiModelProperty(value = "群id",name = "groupId")
    private String groupId;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述",name = "description")
    private String description;

    /**
     * 申请人id
     */
    @ApiModelProperty(value = "申请人id",name = "createdBy")
    private Integer createdBy;

    /**
     * 申请人姓名
     */
    @ApiModelProperty(value = "申请人姓名",name = "creatorName")
    private String creatorName;

    /**
     * 申请人头像
     */
    @ApiModelProperty(value = "申请人头像",name = "headUrl")
    private String headUrl;

    /**
     * 当前受理人id
     */
    @ApiModelProperty(value = "当前受理人id",name = "currentSolverId")
    private Integer currentSolverId;

    /**
     * 当前受理人姓名
     */
    @ApiModelProperty(value = "当前受理人姓名",name = "currentSolverName")
    private String currentSolverName;

    /**
     * 当前受理人头像
     */
    @ApiModelProperty(value = "当前受理人头像",name = "currentSolverHeadUrl")
    private String currentSolverHeadUrl;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态",name = "status")
    private Integer status;

    /**
     * 最新更新时间
     */
    @ApiModelProperty(value = "最新更新时间",name = "lastUpdatedOn")
    private Date lastUpdatedOn;
}
