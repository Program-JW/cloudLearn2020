package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author liqingtian
 * @date 2019/12/27 17:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "通过创建人批量启动实例参数对象", description = "通过创建人批量启动实例参数对象")
public class BatchStartLightningInstanceRequest extends StartInstanceExtRequest {
    @ApiModelProperty(value = "启动实例信息列表",name = "instanceInfoList")
    @NotEmpty(message = "启动实例信息列表不能为空")
    List<InstanceInfoRequest> instanceInfoList;
}
