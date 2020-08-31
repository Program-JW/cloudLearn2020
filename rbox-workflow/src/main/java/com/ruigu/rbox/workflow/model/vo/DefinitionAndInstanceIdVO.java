package com.ruigu.rbox.workflow.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liqingtian
 * @date 2019/12/26 17:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "定义id和实例id实体")
public class DefinitionAndInstanceIdVO {
    @ApiModelProperty(value = "定义id",name = "definitionId")
    private String definitionId;
    @ApiModelProperty(value = "实例id",name = "instanceId")
    private String instanceId;
    @ApiModelProperty(value = "业务key",name = "businessKey")
    private String businessKey;
}
