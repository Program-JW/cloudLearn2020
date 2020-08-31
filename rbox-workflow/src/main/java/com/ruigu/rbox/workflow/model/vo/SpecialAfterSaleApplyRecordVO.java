package com.ruigu.rbox.workflow.model.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ruigu.rbox.workflow.strategy.convert.LocalDateTimeConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/8/12 23:01
 */
@Data
@Builder
@ColumnWidth(20)
@ExcelIgnoreUnannotated
public class SpecialAfterSaleApplyRecordVO {

    @ExcelProperty(value = "序号")
    @ApiModelProperty(value = "序号", name = "id")
    private Long id;

    @ApiModelProperty(value = "申请人编号", name = "userId")
    private Integer userId;

    @ExcelProperty(value = "申请人姓名")
    @ApiModelProperty(value = "申请人姓名", name = "nickName")
    private String nickName;


    @ApiModelProperty(value = "部门编号", name = "deptName")
    private Integer deptNo;

    @ExcelProperty(value = "部门名称")
    @ApiModelProperty(value = "部门名称", name = "deptNo")
    private String deptName;

    @ExcelProperty(value = "审批单号")
    @ApiModelProperty(value = "审批单号", name = "applyCode")
    private String applyCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @ExcelProperty(value = "提交日期", converter = LocalDateTimeConverter.class)
    @ApiModelProperty(value = "提交日期", name = "applyDate")
    private LocalDateTime applyDate;

    @ExcelProperty(value = "状态")
    @ApiModelProperty(value = "状态", name = "status")
    private String status;

    @ExcelProperty(value = "客户名称")
    @ApiModelProperty(value = "客户名称", name = "customerName")
    private String customerName;

    @ExcelProperty(value = "客户级别")
    @ApiModelProperty(value = "客户级别", name = "customerRating")
    private String customerRating;

    @ExcelProperty(value = "特殊申请原因")
    @ApiModelProperty(value = "特殊申请原因", name = "applyReason")
    private String applyReason;

    @ExcelProperty(value = "申请总金额")
    @ApiModelProperty(value = "申请总金额", name = "totalApplyAmount")
    private BigDecimal totalApplyAmount;
}
