package com.ruigu.rbox.workflow.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/11 16:10
 */
@Data
public class SpecialAfterSaleReviewPositionDTO {

    private Integer configId;

    private Integer groupId;

    private String groupName;

    private List<String> positionList;

    private List<Integer> ccIds;
}
