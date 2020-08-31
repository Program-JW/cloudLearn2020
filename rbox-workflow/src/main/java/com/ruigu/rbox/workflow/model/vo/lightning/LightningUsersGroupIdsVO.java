package com.ruigu.rbox.workflow.model.vo.lightning;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author caojinghong
 * @date 2020/01/14 13:49
 */
@Data
public class LightningUsersGroupIdsVO {
    @ApiModelProperty(value = "系统时间", name = "serverTime")
    private Date serverTime;
    @ApiModelProperty(value = "当前用户id", name = "userId")
    private Integer userId;
    @ApiModelProperty(value = "当前用户名称", name = "userName")
    private String userName;
    @ApiModelProperty(value = "是否是领导", name = "leader")
    private Boolean leader;
    @ApiModelProperty(value = "当前用户所属的群id集合", name = "groupIds")
    private List<String> groupIds;
}
