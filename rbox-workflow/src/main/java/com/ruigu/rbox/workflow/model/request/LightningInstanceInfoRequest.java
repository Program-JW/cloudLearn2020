package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * @author liqingtian
 * @date 2019/12/27 18:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LightningInstanceInfoRequest extends InstanceInfoRequest {

    @NotEmpty(message = "受理人不能为空")
    @ApiModelProperty(value = "受理人ID列表")
    private Long receiverId;

    @NotBlank(message = "问题描述不能为空")
    @ApiModelProperty(value = "问题描述")
    private String description;
}
