package com.ruigu.rbox.workflow.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenzhenya
 * @date 2020/5/23 10:16
 */
@Data
public class CcVO {
    /**
     * 抄送人ID
     */
    @ApiModelProperty(name = "userId", value = "抄送人ID")
    private Integer userId;
    /**
     * 抄送人姓名
     */
    @ApiModelProperty(name = "username", value = "抄送人姓名")
    private String username;
    /**
     * 抄送人头像
     */
    @ApiModelProperty(name = "username", value = "抄送人头像")
    private String avatar;
}