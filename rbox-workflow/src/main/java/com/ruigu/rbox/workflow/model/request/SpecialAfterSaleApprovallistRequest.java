package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class SpecialAfterSaleApprovallistRequest {

    @ApiModelProperty(value = "页数", name = "page")
    @Min(value = 0, message = "页数不能小于0")
    private Integer page;

    @ApiModelProperty(value = "分页大小", name = "size")
    private Integer size;

    @ApiModelProperty(value = "申请状态 0-全部", name = "status")
    @NotNull(message = "状态不能为空")
    private Integer status;

   /* *//**
     * 特殊售后审批配置ID
     *//*
    @ApiModelProperty(value = "特殊售后审批配置ID", name = "reviewId", required = true)
    @NotNull(message = "特殊售后审批配置ID不能为空")
    private Long reviewId;*/

}
