package com.ruigu.rbox.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author liqingtian
 * @date 2020/05/08 10:13
 */
@Data
@NoArgsConstructor
public class DutyUserByDayDTO {

    @ApiModelProperty(value = "值班表id", name = "planId")
    private Integer planId;

    @ApiModelProperty(value = "值班人id", name = "userId")
    private Integer userId;

    @ApiModelProperty(value = "值班人姓名", name = "name")
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @ApiModelProperty(value = "值班日期", name = "dutyDate")
    private LocalDate dutyDate;

    @ApiModelProperty(value = "是否可修改", name = "modifiable")
    private Boolean modifiable;
}
