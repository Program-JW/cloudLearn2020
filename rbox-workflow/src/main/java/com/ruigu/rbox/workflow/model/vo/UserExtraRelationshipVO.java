package com.ruigu.rbox.workflow.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.List;

/**
 * @author yuanLin
 * @date 2020-06-03 13:04
 */
@ApiModel(description = "中台用户和第三方应用关联关系")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@FieldNameConstants
public class UserExtraRelationshipVO {
    @ApiModelProperty(value = "用户id")
    private Integer id;
    @ApiModelProperty(value = "用户昵称")
    private String nickname;
    private String wxWorkUserId;
    private String position;
    private String userCode;
    @ApiModelProperty(value = "用户状态 1：正常")
    private Integer status;
    @ApiModelProperty(value = "删除标志 0: 未删除")
    private Integer deleted;
    private List<ExtraInfo> extraInfos;


    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    @FieldNameConstants
    public static class ExtraInfo {
        @ApiModelProperty(value = "用户id")
        private Integer userId;
        @ApiModelProperty(value = "第三方应用id")
        private Integer clientId;
        @ApiModelProperty(value = "第三方应用名称")
        private String clientName;
        @ApiModelProperty(value = "用户在第三方应用的id")
        private String extraUserId;
    }
}
