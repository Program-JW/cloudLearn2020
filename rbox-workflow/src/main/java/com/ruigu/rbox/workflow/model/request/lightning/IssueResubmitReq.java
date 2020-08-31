package com.ruigu.rbox.workflow.model.request.lightning;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author liqingtian
 * @date 2020/03/02 18:50
 */
@Data
public class IssueResubmitReq {

    /**
     * 问题id
     */
    @ApiModelProperty(value = "问题ID", name = "issueId", required = true)
    @NotNull(message = "问题id不能为空")
    private Integer issueId;
}
