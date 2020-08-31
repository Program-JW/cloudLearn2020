package com.ruigu.rbox.workflow.model.request.lightning;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author caojinghong
 * @date 2019/12/31 11:21
 */
@Data
public class QueryMySubmittedReq {

    @ApiModelProperty(value = "查询字段", name = "keyWord")
    private String keyWord;
    @NotNull
    @Min(0)
    @ApiModelProperty(value = "页数,从0开始", name = "page", example = "0", required = true)
    private Integer page;
    @NotNull
    @Min(-1)
    @ApiModelProperty(value = "每页数量", name = "size", example = "10", required = true)
    private Integer size;
    @ApiModelProperty(value = "是否运行中", name = "run")
    private Integer run;
}
