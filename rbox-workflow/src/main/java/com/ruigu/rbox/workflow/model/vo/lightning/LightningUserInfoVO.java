package com.ruigu.rbox.workflow.model.vo.lightning;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author caojinghong
 * @date 2019/12/31 19:03
 */
@Data
@ApiModel(value = "用户信息")
@ToString
public class LightningUserInfoVO {
    @ApiModelProperty(value = "用户id",name = "id")
    private Integer id;
    @ApiModelProperty(value = "用户昵称",name = "nickName")
    private String nickName;
    @ApiModelProperty(value = "用户头像",name = "avatar")
    private String avatar;
    @ApiModelProperty(value = "部门描述",name = "groupDesc")
    private String groupDesc;

}
