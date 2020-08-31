package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author liqingtian
 * @date 2019/12/27 18:24
 */
@Data
public class StartInstanceExtRequest {

    @NotBlank(message = "启动流程的KEY值不能为空")
    @ApiModelProperty(value = "流程key", required = true)
    private String key;

    @ApiModelProperty(value = "来源平台")
    private String sourcePlatform;

    @ApiModelProperty(value = "来源平台用户ID")
    private String sourcePlatformUserId;

    @ApiModelProperty(value = "来源平台用户名称")
    private String sourcePlatformUserName;

    @ApiModelProperty(value = "创建者ID")
    private Long creatorId;
}
