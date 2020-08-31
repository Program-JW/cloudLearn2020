package com.ruigu.rbox.workflow.model.vo.lightning;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author caojinghong
 * @date 2020/01/07 14:20
 */
@ApiModel(value = "问题相关成员信息实体")
@Data
public class LightningIssueRelevantUserVO {
    @ApiModelProperty(value = "用户ID", name = "userId")
    private Integer userId;
    @ApiModelProperty(value = "用户头像", name = "avatar")
    private String avatar;
    @ApiModelProperty(value = "用户昵称", name = "nickName")
    private String nickName;
    @ApiModelProperty(value = "部门描述", name = "groupDesc")
    private String groupDesc;
}
