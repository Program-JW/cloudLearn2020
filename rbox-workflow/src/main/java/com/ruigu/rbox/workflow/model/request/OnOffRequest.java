package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 开关启用
 *
 * @author liqingtian
 * @date 2020/05/07 17:24
 */
@Data
public class OnOffRequest {

    @ApiModelProperty(value = "id", name = "id", required = true)
    @NotNull(message = "id不能为空")
    private Integer id;

    @ApiModelProperty(value = "状态 （0-禁用 1-启用）", name = "status", required = true)
    @NotNull(message = "状态不能为空")
    private Integer status;
}
