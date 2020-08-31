package com.ruigu.rbox.workflow.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/8/15 14:10
 */

@Data
public class SpecialAfterSaleQuotaRuleDTO {

    /**
     * 额度规则编号
     */
    private Integer id;

    private String name;

    /**
     * 额度规则历史编号
     */
    private Integer ruleHistoryId;

    /**
     * 大通系数
     */
    private BigDecimal commonCoefficient;

    /**
     * 非大通系数
     */
    private BigDecimal nonCommonCoefficient;

    /**
     * 组类型 1 城市组 2 BDM组
     */
    private Integer groupType;

    /**
     * 城市组ID或者BDM组ID
     */
    private Integer groupId;

    /**
     * 适应范围 1 电销 2 直销
     */
    private Integer type;


}
