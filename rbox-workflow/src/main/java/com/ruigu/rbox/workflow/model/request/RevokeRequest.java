package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liqingtian
 * @date 2019/10/12 13:39
 */
@Data
@ApiModel(value = "问题/流程撤销请求参数对象", description = "问题/流程撤销请求参数对象")
public class RevokeRequest {

    /**
     * 推推棒撤销 - 流程id
     */
    @ApiModelProperty(value = "实例ID（闪电链不需要传）")
    private String instanceId;

    /**
     * 闪电链撤销 - 问题ids
     */
    @ApiModelProperty(value = "问题ID")
    private Integer issueId;

    /**
     * 闪电链撤销 - 撤销原因
     */
    @ApiModelProperty(value = "撤销原因")
    private String revokeReason;
}
