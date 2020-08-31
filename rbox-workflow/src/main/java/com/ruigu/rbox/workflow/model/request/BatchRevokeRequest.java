package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/01/13 21:14
 */
@Data
@ApiModel(value = "批量问题撤销请求")
public class BatchRevokeRequest {

    @NotEmpty(message = "批量撤销问题列表不能为空")
    @ApiModelProperty("批量问题撤销信息列表")
    List<RevokeRequest> revokeIssueList;
}
