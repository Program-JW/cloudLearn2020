package com.ruigu.rbox.workflow.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LeaveReportTypeDTO {
    /**
     * 请假报备类型ID
     */
    @ApiModelProperty(name = "typeId", value = "请假报备类型ID", required = true)
    private Integer typeId;
    /**
     * 请假报备类型名称
     */
    @ApiModelProperty(name = "typeName", value = "请假报备类型名称", required = true)
    private String typeName;
}