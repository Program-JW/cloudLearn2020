package com.ruigu.rbox.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruigu.rbox.workflow.model.vo.DutyUserByWeekVO;
import lombok.Data;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/06/04 19:44
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DutyUserDetailDTO {

    /**
     * 按天
     */
    private List<DutyUserByDayDTO> dayList;

    /**
     * 按周
     */
    private List<DutyUserByWeekVO> weekList;

    /**
     * 展示使用 （入参不填）
     */
    private List<DutyUserByPollDTO> pollList;
}
