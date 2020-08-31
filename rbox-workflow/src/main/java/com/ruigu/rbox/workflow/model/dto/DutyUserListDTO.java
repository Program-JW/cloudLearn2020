package com.ruigu.rbox.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruigu.rbox.workflow.model.vo.DutyUserByWeekVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/06/04 19:56
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DutyUserListDTO {

    /**
     * 按天
     */
    @ApiModelProperty(value = "按天值班人信息", name = "dayList")
    private Page<DutyUserByDayDTO> dayList;

    /**
     * 按周
     */
    @ApiModelProperty(value = "按周值班人信息", name = "weekList")
    private List<DutyUserByWeekVO> weekList;

    /**
     * 展示使用 （入参不填）
     */
    @ApiModelProperty(value = "轮询值班人信息", name = "pollList")
    private List<DutyUserByPollDTO> pollList;
}
