package com.ruigu.rbox.workflow.model.request.lightning;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author caojinghong
 * @date 2019/12/27 20:10
 */
@Data
public class IssueConfirmReq {
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

    @ApiModelProperty(value = "状态：已解决(true) / 未解决(false)", name = "resolved", required = true)
    @NotNull(message = "状态不能为空")
    private Boolean resolved;
    @ApiModelProperty(value = "未解决原因",name = "unsolvedReason")
    private String unsolvedReason;


}
