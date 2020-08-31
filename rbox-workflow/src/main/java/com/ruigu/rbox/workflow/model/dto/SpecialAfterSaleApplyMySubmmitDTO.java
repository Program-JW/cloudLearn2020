package com.ruigu.rbox.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SpecialAfterSaleApplyMySubmmitDTO {
    /**
     *创建人
     */
    private Integer createBy;

    /**
     * 申请人姓名
     */
    private String applyNickname;

    /**
     * 审批状态
     */
    private Integer status;

    /**
     * 申请金额
     */
    private BigDecimal totalApplyAmount;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalDateTime createdAt;

    /**
     * 审批时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private  LocalDateTime approvalTime;



}
