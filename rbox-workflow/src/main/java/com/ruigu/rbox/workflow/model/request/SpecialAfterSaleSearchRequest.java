package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author liqingtian
 * @date 2020/08/10 16:51
 */
@Data
public class SpecialAfterSaleSearchRequest {

    @ApiModelProperty(value = "搜索关键词", name = "keyWord")
    private String keyWord;


    @ApiModelProperty(value = "页数", name = "page")
    @Min(value = 0, message = "页数不能小于0")
    @NotNull(message = "页数不能为空")
    private Integer page;

    @ApiModelProperty(value = "分页大小", name = "size")
    @Min(value = 1, message = "每页大小不能小于1")
    @NotNull(message = "每页大小不能为空")
    private Integer size;

    @ApiModelProperty(value = "申请状态 0-全部", name = "status")
    @NotNull(message = "状态不能为空")
    private Integer status;
}
