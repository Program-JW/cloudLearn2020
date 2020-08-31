package com.ruigu.rbox.workflow.model.request;

import com.ruigu.rbox.workflow.model.dto.DutyUserByDayDTO;
import com.ruigu.rbox.workflow.model.dto.DutyUserByWeekDTO;
import com.ruigu.rbox.workflow.model.vo.DutyUserByWeekVO;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 值班人
 *
 * @author liqingtian
 * @date 2020/06/03 18:01
 */
@Data
public class DutyUserRequest {

    /**
     * 按天
     */
    private List<DutyUserByDayDTO> dayList;


    /**
     * 按周
     */
    private List<DutyUserByWeekDTO> weekList;

    /**
     * 轮询
     */
    private List<Integer> pollList;

}
