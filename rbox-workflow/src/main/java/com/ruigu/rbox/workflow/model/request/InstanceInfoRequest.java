package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/12/27 18:35
 */
@Data
@ApiModel(value = "启动实例信息")
public class InstanceInfoRequest {

    @ApiModelProperty(value = "业务数据主键")
    private String businessKey;

    @ApiModelProperty(value = "业务数据H5地址")
    private String businessUrl;

    @ApiModelProperty(value = "自定义的流程实例名称")
    private String name;

    @ApiModelProperty(value = "归属人ID,空则跟创建人一致")
    private Long ownerId;

    @ApiModelProperty(value = "要提交到流程中的参数对象")
    private Map<String, Object> variables;

    @ApiModelProperty(value = "单据业务参数")
    private Map<String, Object> businessParams;
}
