package com.ruigu.rbox.workflow.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author liqingtian
 * @date 2020/05/07 14:53
 */
@Data
@NoArgsConstructor
public class LightningCategoryConfigVO {

    /**
     * 主键
     */
    @ApiModelProperty(value = "问题分类id", name = "categoryId", required = true)
    private Integer categoryId;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "问题分类名称", name = "categoryName", required = true)
    private String categoryName;

    /**
     * 处理人ID
     */
    @ApiModelProperty(value = "处理人id", name = "userId", required = true)
    private Integer userId;

    /**
     * 处理人名称
     */
    @ApiModelProperty(value = "处理人名称", name = "userName", required = true)
    private String userName;

    /**
     * 绑定的规则ID
     */
    @ApiModelProperty(value = "绑定的规则id", name = "ruleId", required = true)
    private Integer ruleId;

    /**
     * 绑定的规则名称
     */
    @ApiModelProperty(value = "绑定的规则名称", name = "ruleName", required = true)
    private String ruleName;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", name = "sort", required = true)
    private Integer sort;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态", name = "status", required = true)
    private Integer status;
}
