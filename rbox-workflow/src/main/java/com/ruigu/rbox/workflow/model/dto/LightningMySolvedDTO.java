package com.ruigu.rbox.workflow.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * @author caojinghong
 * @date 2020/01/06 15:06
 */
@ApiModel(value = "已提交列表查询结果实体")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LightningMySolvedDTO {
    @ApiModelProperty(value = "问题id",name = "issueId")
    private Integer issueId;
    @ApiModelProperty(value = "问题描述",name = "description")
    private String description;
    @ApiModelProperty(value = "问题状态",name = "status")
    private Integer status;
    @ApiModelProperty(value = "是否系统自动确认 0-否 1-是",name = "autoConfirm")
    private Integer autoConfirm;
    @ApiModelProperty(value = "当前解决对象id",name = "currentSolverId")
    private Integer currentSolverId;
    @ApiModelProperty(value = "当前解决对象名称",name = "currentSolverName")
    private String currentSolverName;
    @ApiModelProperty(value = "当前解决对象头像",name = "currentSolverAvatar")
    private String currentSolverAvatar;
    @ApiModelProperty(value = "申请人头像",name = "headUrl")
    private String headUrl;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "更新时间",name = "lastUpdatedOn")
    private Date lastUpdatedOn;
    @ApiModelProperty(value = "群id",name = "groupId")
    private String groupId;
    @ApiModelProperty(value = "群名称",name = "groupName")
    private String groupName;
}
