package com.ruigu.rbox.workflow.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/8/18 15:24
 */
@Data
public class SpecialAfterSaleQuotaRuleVO {
    @ApiModelProperty(value = "审批配置编号", name = "审批配置编号")
    private Integer id;

    @ApiModelProperty(value = "审批额度配置名称", name = "name")
    private String name;

    /**
     * 大通系数
     */
    @NotNull(message = "大通系数不能为空")
    @ApiModelProperty(value = "大通系数", name = "commonCoefficient")
    private BigDecimal commonCoefficient;

    /**
     * 非大通系数
     */
    @NotNull(message = "非大通系数不能为空")
    @ApiModelProperty(value = "非大通系数", name = "nonCommonCoefficient")
    private BigDecimal nonCommonCoefficient;

    /**
     * 组类型 1 城市组 2 BDM组
     */
    @NotNull(message = "组类型不能为空")
    @ApiModelProperty(value = "组类型(1 城市组 2 BDM组)", name = "groupType")
    private Integer groupType;

    /**
     * 城市组ID或者BDM组ID
     */
    @ApiModelProperty(value = "城市组ID或者BDM组ID", name = "groupIds")
    private List<Integer> groupIds;

    @ApiModelProperty(value = "城市组名称或者BDM组名称", name = "groupNames")
    private List<String> groupNames;

    /**
     * 适应范围 1 电销 2 直销
     */
    @NotNull(message = "适应范围不能为空")
    @ApiModelProperty(value = "适应范围(1 电销 2 直销)", name = "type")
    private Integer type;
}
