package com.ruigu.rbox.workflow.model.vo.lightning;

import com.ruigu.rbox.workflow.model.entity.LightningIssueEvaluationEntity;
import com.ruigu.rbox.workflow.model.entity.TaskEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author caojinghong
 * @date 2019/12/31 18:56
 */
@ApiModel(value = "查询问题详情实体")
@Data
public class LightningIssueItemVO {
    @ApiModelProperty(value = "当前登录用户id",name = "userId")
    private Integer userId;
    @ApiModelProperty(value = "当前登录用户名称",name = "userName")
    private String userName;
    @ApiModelProperty(value = "当前登录用户问题角色",name = "userIssueRole")
    private Integer userIssueRole;
    @ApiModelProperty(value = "当前问题的任务实体",name = "task")
    private TaskEntity task;
    @ApiModelProperty(value = "问题描述",name = "description")
    private String description;
    @ApiModelProperty(value = "群ID",name = "groupId")
    private String groupId;
    @ApiModelProperty(value = "群名称",name = "groupName")
    private String groupName;
    @ApiModelProperty(value = "状态 -1 作废 0 发起 1 待受理 2 受理中 3 待确认 4 已解决 5 未解决", name = "status")
    private Integer status;
    @ApiModelProperty(value = "是否系统自动确认  0-否，1-是",name = "autoConfirm")
    private Integer autoConfirm;
    @ApiModelProperty(value = "创建时间", name = "createdOn")
    private Date createdOn;
    @ApiModelProperty(value = "申请人信息",name = "applicant")
    private LightningUserInfoVO applicant;
    @ApiModelProperty(value = "评价信息",name = "evaluation")
    private LightningIssueEvaluationEntity evaluation;

    @ApiModelProperty(value = "问题图片集合",name = "attachments")
    private List<String> attachments;
    @ApiModelProperty(value = "日志记录集合",name = "logs")
    private List<LightningIssueLogVO> logs;
    @ApiModelProperty(value = "问题相关成员信息集合",name = "relevantUserVos")
    private List<LightningIssueRelevantUserVO> relevantUserVos;

    @ApiModelProperty(value = "状态具体详情",name = "statusMap")
    private Map<Integer,String> statusMap;
    @ApiModelProperty(value = "操作类型具体详情",name = "logTypeMap")
    private Map<Integer,String> logTypeMap;
}
