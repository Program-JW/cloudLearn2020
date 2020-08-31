package com.ruigu.rbox.workflow.model.vo;

import com.ruigu.rbox.workflow.model.dto.DutyUserListDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liqingtian
 * @date 2020/05/08 16:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LightningDutyRuleSelectVO extends LightningDutyRuleVO {

    @ApiModelProperty(value = "值班人信息", name = "dutyUser")
    private DutyUserListDTO dutyUser;
}
