package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/01/06 10:57
 */
@Data
@ApiModel(value = "闪电链我受理列表")
@NoArgsConstructor
public class LightningMyAcceptanceRequest {

    @ApiModelProperty(value = "搜索关键字")
    private String keyword;

    @ApiModelProperty(value = "关键字所匹配用户id列表")
    private List<Integer> userIds;

    @ApiModelProperty(value = "分页页码")
    private Integer page;

    @ApiModelProperty(value = "页大小")
    private Integer size;

    @ApiModelProperty(value = "是否正在运行")
    @NotNull(message = "缺少run值")
    private Integer run;
}
