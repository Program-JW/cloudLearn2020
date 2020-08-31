package com.ruigu.rbox.workflow.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GroupInfoDTO {
    /**
     * 组ID
     */
    @ApiModelProperty(name = "groupId", value = "组ID", required = true)
    private Integer groupId;
    /**
     * 组名称
     */
    @ApiModelProperty(name = "groupName", value = "组名称", required = true)
    private String groupName;
}