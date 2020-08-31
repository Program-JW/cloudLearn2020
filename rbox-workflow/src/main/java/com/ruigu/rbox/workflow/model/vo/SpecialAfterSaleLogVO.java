package com.ruigu.rbox.workflow.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/10 19:16
 */
@Data
public class SpecialAfterSaleLogVO {
    @ApiModelProperty(value = "日志id", name = "id")
    private Long id;
    @ApiModelProperty(value = "节点id", name = "reviewNodeId")
    private Integer reviewNodeId;
    @ApiModelProperty(value = "动作", name = "action")
    private Integer action;
    @ApiModelProperty(value = "备注", name = "remarks")
    private String remarks;
    @ApiModelProperty(value = "日志操作人", name = "operatorIdList")
    private List<Integer> operatorIdList;
    @ApiModelProperty(value = "日志操作人名称", name = "createdBy")
    private List<String> operatorNameList;
    @ApiModelProperty(value = "创建时间", name = "createdAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;
    @ApiModelProperty(value = "是否展示", name = "show")
    private Integer show;
}
