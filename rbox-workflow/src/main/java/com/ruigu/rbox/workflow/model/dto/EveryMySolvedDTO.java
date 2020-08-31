package com.ruigu.rbox.workflow.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author caojinghong
 * @date 2020/01/09 11:45
 */
@Data
public class EveryMySolvedDTO {
    @ApiModelProperty(value = "问题id",name = "issueId")
    private Integer issueId;
    @ApiModelProperty(value = "问题描述",name = "description")
    private String description;
    @ApiModelProperty(value = "问题状态",name = "status")
    private Integer status;
    @ApiModelProperty(value = "当前解决对象id",name = "currentSolverId")
    private Integer currentSolverId;
    @ApiModelProperty(value = "当前解决对象名称",name = "currentSolverName")
    private String currentSolverName;
    @ApiModelProperty(value = "当前解决对象头像",name = "currentSolverAvatar")
    private String currentSolverAvatar;
}
