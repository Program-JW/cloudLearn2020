package com.ruigu.rbox.workflow.model.request.lightning;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author caojinghong
 * @date 2020/02/26 14:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryMyParticipatedReq extends QueryMySubmittedReq {
    @NotNull
    @ApiModelProperty(value = "是否过滤状态是“已解决/已撤销的问题”", name = "filter", example = "true", required = true)
    private Boolean filter;
}
