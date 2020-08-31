package com.ruigu.rbox.workflow.model.vo;

import com.ruigu.rbox.workflow.model.dto.DutyUserDetailDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liqingtian
 * @date 2020/05/08 16:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LightningDutyRuleDetailVO extends LightningDutyRuleVO {

    @ApiModelProperty(value = "值班数据", name = "dutyUser")
    private DutyUserDetailDTO dutyUser;
}
