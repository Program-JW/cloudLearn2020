package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 分类请求
 *
 * @author liqingtian
 * @date 2020/05/07 14:26
 */
@Data
public class LightningCategoryRequest {

    @ApiModelProperty(value = "问题分类id", name = "categoryId", required = true)
    private Integer categoryId;

    @ApiModelProperty(value = "问题分类名称", name = "categoryName", required = true)
    @NotBlank(message = "问题分类名称不能为空")
    private String categoryName;

    @ApiModelProperty(value = "处理人id", name = "userId", required = true)
    private Integer userId;

    @ApiModelProperty(value = "绑定规则id", name = "ruleId", required = true)
    private Integer ruleId;

    @ApiModelProperty(value = "分类排序", name = "sort", required = true)
    @NotNull(message = "分类排序不能为空")
    private Integer sort;
}
