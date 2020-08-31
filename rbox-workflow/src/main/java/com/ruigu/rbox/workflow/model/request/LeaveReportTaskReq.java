package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author wuyimin
 * @Date 2020/5/24 1:45
 */
@Data
@NoArgsConstructor
@ApiModel(description = "查询请假报备状态条件")
public class LeaveReportTaskReq {

    @ApiModelProperty(name = "id", value = "更新的时候用", required = true)
    private Integer id;

    /**
     * 申请请假报备记录ID
     */
    @ApiModelProperty(name = "applyId", value = "申请记录ID", required = true)
    private Integer applyId;

    /**
     * 流程任务ID
     */
    @ApiModelProperty(name = "taskId", value = "流程任务ID", required = true)
    private String taskId;

    /**
     * 节点序号
     */
    @ApiModelProperty(name = "nodeId", value = "节点序号", required = true)
    private Integer nodeId;

    /**
     * 审批人
     */
    @ApiModelProperty(name = "userId", value = "提交人Id", required = true)
    private List<Integer> userId;

    /**
     * 状态： -1 作废 0 待审批 1 审批中 3 已通过 4 已驳回
     */
    @ApiModelProperty(name = "status", value = "状态： -1 作废 0 待审批 1 审批中 3 已通过 4 已驳回", required = true)
    private Integer status;

    /**
     * 创建人
     */
    @ApiModelProperty(name = "createdBy", value = "创建人", required = true)
    private Integer createdBy;
}
