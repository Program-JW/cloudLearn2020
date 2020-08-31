package com.ruigu.rbox.workflow.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author alan.zhao
 */
@Data
@ApiModel(value = "表单")
public class Form {
    @ApiModelProperty(value = "表单内容",name = "definition")
    private String definition;
    @ApiModelProperty(value = "任务详情表单数据",name = "data")
    private Map<String,Object> data;
}
