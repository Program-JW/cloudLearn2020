package com.ruigu.rbox.workflow.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liqingtian
 * @date 2020/08/13 13:54
 */
@Data
public class RsGroupGmvDataDTO {

    private Integer groupId;

    private String groupName;

    private Integer groupType;

    private GmvInfo gmvInfo;

    @Data
    public static class GmvInfo {
        private BigDecimal commonGmv;
        private BigDecimal notCommonGmv;
    }
}
