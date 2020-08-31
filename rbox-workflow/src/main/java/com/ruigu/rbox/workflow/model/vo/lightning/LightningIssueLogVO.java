package com.ruigu.rbox.workflow.model.vo.lightning;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author caojinghong
 * @date 2019/12/31 19:09
 */

@Data
@ApiModel(value = "问题详情操作日志记录")
public class LightningIssueLogVO {
    @ApiModelProperty(value = "操作人", name = "userId")
    private Integer userId;
    @ApiModelProperty(value = "操作人头像", name = "avatar")
    private String avatar;
    @ApiModelProperty(value = "操作人昵称", name = "nickName")
    private String nickName;
    @ApiModelProperty(value = "操作人部门", name = "groupDesc")
    private String groupDesc;
    @ApiModelProperty(value = "操作时间", name = "operatingTime")
    private Date operatingTime;
    @ApiModelProperty(value = "操作类型，0 发起 1 已受理 2 已交接 3 提交确认 4 确认已解决 5 确认未解决 10 待受理", name = "operatingType")
    private Integer operatingType;
    @ApiModelProperty(value = "时间间隔", name = "duration")
    private Long duration;
}
