package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author liqingtian
 * @date 2019/12/26 11:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "启动闪电链问题实例参数对象", description = "启动实例参数对象")
public class StartLightningInstanceRequest extends StartInstanceExtRequest {
    @ApiModelProperty(value = "启动的流程实例信息", name = "instanceInfo", required = true)
    @NotNull(message = "启动的流程实例信息不能为空")
    private InstanceInfoRequest instanceInfo;
}
