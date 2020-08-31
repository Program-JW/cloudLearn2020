package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/8/13 9:25
 */

@Data
public class SpecialAfterSaleApplyRequest extends SpecialAfterSaleApplyExportRequest {


    @ApiModelProperty(value = "页数", name = "page")
    @Min(value = 0, message = "页数不能小于0")
    @NotNull(message = "页数不能为空")
    private Integer page;

    @ApiModelProperty(value = "分页大小", name = "size")
    @Min(value = 1, message = "分页大小不能小于1")
    @NotNull(message = "分页大小不能为空")
    private Integer size;

}
