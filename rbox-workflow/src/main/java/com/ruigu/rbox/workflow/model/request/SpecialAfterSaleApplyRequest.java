package com.ruigu.rbox.workflow.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/8/13 9:25
 */

@Data
public class SpecialAfterSaleApplyRequest {


    @ApiModelProperty(value = "申请人编号", name = "userId")
    private Integer userId;

    @ApiModelProperty(value = "申请人姓名", name = "userName")
    private String userName;

    @ApiModelProperty(value = "申请人部门id", name = "deptId")
    private Integer deptId;

    @ApiModelProperty(value = "申请人部门名称", name = "deptName")
    private String deptName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @ApiModelProperty(value = "开始时间", name = "startTime")
    private LocalDate startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @ApiModelProperty(value = "结束时间", name = "endTime")
    private LocalDate endTime;

    @ApiModelProperty(value = "状态(-1 作废 0 待审批 1 审批中 3 已通过 4 已驳回)", name = "status")
    private Integer status;

    @ApiModelProperty(value = "页数", name = "page")
    @Min(value = 0, message = "页数不能小于0")
    private Integer page;

    @ApiModelProperty(value = "分页大小", name = "size")
    private Integer size;

}
