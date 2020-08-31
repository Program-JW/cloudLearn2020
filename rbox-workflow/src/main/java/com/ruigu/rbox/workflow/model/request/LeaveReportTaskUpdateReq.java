package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wuyimin
 * @date 2020/5/27 10:43
 */
@Data
@ApiModel(description = "审批人更新 提交审批的请假报备状态")
public class LeaveReportTaskUpdateReq {
    @ApiModelProperty(name = "taskId", value = "请假报备状态更新", required = true)
    private String taskId;

    @ApiModelProperty(name = "applyId", value = "报备Id", required = true)
    private Integer applyId;

    @ApiModelProperty(name = "id", value = "更新的时候用", required = true)
    private Integer userId;

    @ApiModelProperty(name = "status", value = "状态： -1 作废 0 待审批 1 审批中 3 已通过 4 已驳回", required = true)
    private Integer status ;

}
