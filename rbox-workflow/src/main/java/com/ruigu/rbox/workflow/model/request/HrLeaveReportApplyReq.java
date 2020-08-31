package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author wuyimin
 * @date 2020/5/22 16:09
 */
@Data
@ApiModel("请假报备申请参数")
public class HrLeaveReportApplyReq implements Serializable {

    private Integer id;

    @ApiModelProperty(name = "applyUserId", value = "提交人Id ，内部生成", notes = "内部生成", required = true)
    private Integer applyUserId;

    /**
     * 请假报备类型ID
     */
    @ApiModelProperty(name = "leaveReportTypeId", value = "请假报备类型ID，选中请假类型，传入", notes = "选中请假类型，传入", required = true)
    private Integer leaveReportTypeId;

    /**
     * 请假报备配置ID
     */
    @ApiModelProperty(name = "reviewConfigId", value = "请假报备配置ID", notes = "请假报备配置ID", required = false)
    private Integer reviewConfigId;


    @ApiModelProperty(name = "startDate", value = "请假报备开始时间 （按天）", required = false)
    private String startDate;

    @ApiModelProperty(name = "start", value = "开始时间 上下午标志位 （0-上午 1-下午） （按天）", required = false)
    private Integer start;

    @ApiModelProperty(name = "endDate", value = "请假报备结束时间 （按天）", required = false)
    private String endDate;

    @ApiModelProperty(name = "end", value = "结束时间 上下午标志位 （0-上午 1-下午） （按天）", required = false)
    private Integer end;

    /**
     * 请假报备开始时间
     */
    private String startTime;

    /**
     * 请假报备结束时间
     */
    private String endTime;

    /**
     * 请假报备时长：按时间刻度存储
     */
    @ApiModelProperty(name = "duration", value = "请假报备时长，根据时间刻度选择，前端计算得出",notes = "根据时间刻度选择，前端计算得出")
    private Double duration;

    /**
     * 请假报备时长：按天存储
     */
    @ApiModelProperty(name = "durationDay", value = "请假报备时长：按天存储（后台生产）" )
    private BigDecimal durationDay;

    /**
     * 流程定义ID
     */
    @ApiModelProperty(name = "definitionId", value = "流程定义ID，根据时间刻度选择，内部",notes = "根据时间刻度选择，内部", required = true )
    private String definitionId;

    /**
     * 流程实例ID
     */
    @ApiModelProperty(name = "instanceId", value = "流程实例ID，根据时间刻度选择，内部",notes = "根据时间刻度选择，内部", required = true)
    private String instanceId;

    /**
     * 状态： -1 作废 0 待审批 1 审批中 3 已通过 4 已驳回
     */
    @ApiModelProperty(name = "status", value = "状态,根据时间刻度选择，内部", notes = "根据时间刻度选择，内部" )
    private Integer status;

    /**
     * 创建人
     */
    @ApiModelProperty(name = "createdBy", value = "创建人，根据时间刻度选择，内部",notes = "根据时间刻度选择，内部" )
    private Integer createdBy;

    /**
     * 最后修改人
     */
    @ApiModelProperty(name = "lastUpdateBy", value = "最后修改人，根据时间刻度选择，内部",notes = "根据时间刻度选择，内部")
    private Integer lastUpdateBy;

    /**
     * 请假理由
     */
    @ApiModelProperty(name = "applyReason", value = "请假理由，输入的理由，传入",notes = "输入的理由，传入")
    private String applyReason;

    /**
     * 抄送人ID集合
     */
    @ApiModelProperty(name = "ccIdList", value = "抄送人ID集合")
    private List<Integer> ccIdList;

}
