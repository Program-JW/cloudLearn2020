package com.ruigu.rbox.workflow.model.dto;

import lombok.Data;

/**
 * @author liqingtian
 * @date 2019/11/14 16:41
 */
@Data
public class PassportUserInfoDTO {
    private Integer id;
    private String userCode;
    private String wxWorkUserId;
    private String username;
    private String nickname;
    private String cnName;
    private String enName;
    private String alias;
    private String position;
    private Integer type;
    private Integer source;
    private String salt;
    private String description;
    private String avatar;
    private String qrCode;
    private String mobile;
    private String telephone;
    private String email;
    private Integer status;
    private Integer deleted;
    private Integer gender;
    private String wxOpenId;
    private String wxUnionId;
    private String wxWorkOpenId;
    private String createdOn;
    private String lastUpdatedOn;
    private Integer createdBy;
    private Integer lastUpdatedBy;
}
