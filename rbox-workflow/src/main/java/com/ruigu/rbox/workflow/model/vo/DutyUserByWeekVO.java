package com.ruigu.rbox.workflow.model.vo;

import com.ruigu.rbox.workflow.model.dto.DutyUserByPollDTO;
import lombok.Data;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/06/04 19:53
 */
@Data
public class DutyUserByWeekVO {

    private Integer id;

    private Integer dayOfWeek;

    private List<DutyUserByPollDTO> dutyUserList;

}
