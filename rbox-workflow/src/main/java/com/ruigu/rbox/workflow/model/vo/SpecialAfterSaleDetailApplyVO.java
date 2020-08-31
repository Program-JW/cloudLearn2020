package com.ruigu.rbox.workflow.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ruigu.rbox.workflow.model.dto.SpecialAfterSaleGroupQuotaDTO;
import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleDetailEntity;
import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleReviewNodeEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2020/08/10 14:01
 */
@Data
public class SpecialAfterSaleDetailApplyVO {

    @ApiModelProperty(value = "申请id", name = "applyId")
    private Long id;
    @ApiModelProperty(value = "审批单号", name = "code")
    private String code;
    @ApiModelProperty(value = "是否大通 0-非大通 1-大通", name = "common")
    private Integer common;
    @ApiModelProperty(value = "区域id", name = "areaId")
    private Integer areaId;
    @ApiModelProperty(value = "区域名称", name = "areaName")
    private String areaName;
    @ApiModelProperty(value = "城市id", name = "cityId")
    private Integer cityId;
    @ApiModelProperty(value = "城市名称", name = "cityName")
    private String cityName;
    @ApiModelProperty(value = "客户id", name = "customerId")
    private Integer customerId;
    @ApiModelProperty(value = "客户名称", name = "customerName")
    private String customerName;
    @ApiModelProperty(value = "客户手机号", name = "customerPhone")
    private String customerPhone;
    @ApiModelProperty(value = "客户等级", name = "customerRating")
    private String customerRating;
    @ApiModelProperty(value = "申请单类型 1 退货 2 换货 3 补金币", name = "type")
    private Integer type;
    @ApiModelProperty(value = "申请原因", name = "applyReason")
    private String applyReason;
    @ApiModelProperty(value = "需要支持", name = "needSupport")
    private String needSupport;
    @ApiModelProperty(value = "创建时间", name = "createdAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;
    @ApiModelProperty(value = "创建人", name = "creatorName")
    private Integer creatorId;
    @ApiModelProperty(value = "创建人名称", name = "creatorName")
    private String creatorName;
    @ApiModelProperty(value = "创建人部门名", name = "creatorGroupName")
    private String creatorGroupName;
    @ApiModelProperty(value = "状态", name = "status")
    private Integer status;

    @ApiModelProperty(value = "流程id", name = "instanceId")
    private String instanceId;
    @ApiModelProperty(value = "任务id", name = "taskId")
    private String taskId;
    @ApiModelProperty(value = "任务审批人")
    private List<Integer> currentApproverIdList;

    @ApiModelProperty(value = "子表详情", name = "details")
    List<SpecialAfterSaleDetailEntity> details;
    @ApiModelProperty(value = "日志详情", name = "logs")
    List<SpecialAfterSaleLogVO> logs;
    @ApiModelProperty(value = "字典", name = "dictionaries")
    Map<Integer, String> dictionaries;

    @ApiModelProperty(value = "是否电销转审节点", name = "dxManagerNode")
    private Integer dxManagerNode;
    // 当前节点信息
    @ApiModelProperty(value = "当前节点配置信息", name = "reviewNodeInfo")
    private SpecialAfterSaleReviewNodeEntity reviewNodeInfo;
    // 当前审批人的额度
    private Map<Integer, List<SpecialAfterSaleGroupQuotaDTO>> approverQuota;
}
