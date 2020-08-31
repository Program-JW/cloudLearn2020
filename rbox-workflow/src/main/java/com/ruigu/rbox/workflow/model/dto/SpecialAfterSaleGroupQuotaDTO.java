package com.ruigu.rbox.workflow.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liqingtian
 * @date 2020/08/19 0:43
 */
@Data
public class SpecialAfterSaleGroupQuotaDTO {

    private Integer groupId;

    private String groupName;

    private Integer quotaId;

    private BigDecimal surplusQuota;

    private BigDecimal initialQuota;
}
