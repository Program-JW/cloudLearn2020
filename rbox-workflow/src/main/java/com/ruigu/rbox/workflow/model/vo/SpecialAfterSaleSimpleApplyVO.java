package com.ruigu.rbox.workflow.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author liqingtian
 * @date 2020/08/10 16:39
 */
@Data
@Accessors(chain = true)
public class SpecialAfterSaleSimpleApplyVO {

    @ApiModelProperty(value = "申请id", name = "applyId")
    private Long applyId;

    @ApiModelProperty(value = "审批单号", name = "code")
    private String code;

    @ApiModelProperty(value = "申请人id", name = "applyUserId")
    private Integer applyUserId;

    @ApiModelProperty(value = "申请人姓名", name = "applyUserName")
    private String applyUserName;

    @ApiModelProperty(value = "申请金额", name = "applyAmount")
    private BigDecimal applyAmount;

    @ApiModelProperty(value = "申请时间", name = "applyTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime applyTime;

    @ApiModelProperty(value = "审批时间", name = "approvalTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime approvalTime;

    @ApiModelProperty(value = "状态", name = "status")
    private Integer status;
}
