package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author chenzhenya
 * @date 2020/5/23 10:17
 */
@Data
@NoArgsConstructor
@ApiModel("审核配置接口")
public class GetOneReviewConfigReq {
    @ApiModelProperty(name = "applyUserId", value = "申请人ID，使用token调用可不传值")
    private Integer applyUserId;
    @ApiModelProperty(name = "leaveReportTypeId", value = "请假报备类型ID", required = true)
    @NotNull(message = "请假报备类型不能为空")
    private Integer leaveReportTypeId;
    @ApiModelProperty(name = "duration", value = "请假时长", required = true)
    @NotNull(message = "请假时长不能为空")
    private Double duration;
}
