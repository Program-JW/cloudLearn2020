package com.ruigu.rbox.workflow.model.request.lightning;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/04/10 10:35
 */
@Data
public class QueryByGroupReq {

    @ApiModelProperty(value = "群组ID列表", name = "groupIds")
    @NotNull(message = "所要查询群组id列表不能为空")
    private List<String> groupIds;

    @ApiModelProperty(value = "分页页数", name = "page")
    @Min(0)
    private Integer page;

    @ApiModelProperty(value = "分页大小", name = "size")
    @Min(-1)
    private Integer size;
}
