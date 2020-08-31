package com.ruigu.rbox.workflow.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/06/03 18:43
 */
@Data
public class DutyUserByWeekDTO {

    private Integer id;

    private Integer dayOfWeek;

    private List<Integer> dutyUserIdList;
}
