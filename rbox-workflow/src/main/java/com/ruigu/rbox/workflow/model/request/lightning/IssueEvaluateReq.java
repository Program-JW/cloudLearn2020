package com.ruigu.rbox.workflow.model.request.lightning;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author caojinghong
 * @date 2020/01/07 15:40
 */
@Data
public class IssueEvaluateReq {
    /**
     * 问题id
     */
    @ApiModelProperty(value = "问题id", name = "issueId", required = true)
    @NotNull(message = "问题id不能为空")
    private Integer issueId;
    /**
     * 评价分数
     */
    @ApiModelProperty(value = "评价分数", name = "score")
    @NotNull(message = "评价分数不能为空")
    private Integer score;
    /**
     * 最佳处理人id
     */
    @ApiModelProperty(value = "最佳处理人id", name = "bestPersonId")
    private Integer bestPersonId;
    /**
     * 最佳处理人名称
     */
    @ApiModelProperty(value = "最佳处理人名称", name = "bestPersonName")
    private String bestPersonName;
}
