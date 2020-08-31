package com.ruigu.rbox.workflow.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *
 * @author     ：jianghuilin
 * @date       ：Created in {2019/9/16} {11:36}
 */
@ApiModel(value = "该实例的流程状态")
@Data
public class PurchaseOrderStatusVO {
    @ApiModelProperty(value = "该实例操作内容（处理过程）",name = "process")
    private String process;
    @ApiModelProperty(value = "创建时间",name = "createdOn")
    private Date createdOn;
}
