package com.ruigu.rbox.workflow.model.dto;

import com.ruigu.rbox.workflow.model.vo.lightning.UserRoleInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuanLin
 * @date 2020-03-16 16:05
 */
@ApiModel(description = "部门人员搜索接口VO")
@Data
@ToString
public class PassportUserAndGroupDTO {
    @ApiModelProperty(name = "类型", required = false, value = "1代表部门，2代表人")
    private Integer voType;
    //用户信息
    private Integer userId;
    private String userCode;
    private String wxWorkUserId;
    private String username;
    private String nickname;
    private String cnName;
    private String enName;
    private String alias;
    private String position;
    private Integer userType;
    private String userDescription;
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

    //部门信息
    private Integer groupId;
    private String groupName;
    private Integer groupType;
    private String leader;
    private String groupDescription;
    private Integer parentId;

    //角色信息
    private List<UserRoleInfoVO> roles;

    private List<PassportUserAndGroupDTO> childList = new ArrayList<>();

    public PassportUserInfoDTO toUserInfo() {
        PassportUserInfoDTO userInfoDTO = new PassportUserInfoDTO();
        userInfoDTO.setId(this.userId);
        userInfoDTO.setUserCode(this.userCode);
        userInfoDTO.setWxWorkUserId(this.wxWorkUserId);
        userInfoDTO.setUsername(this.username);
        userInfoDTO.setNickname(this.nickname);
        userInfoDTO.setCnName(this.cnName);
        userInfoDTO.setEnName(this.enName);
        userInfoDTO.setAlias(this.alias);
        userInfoDTO.setPosition(this.position);
        userInfoDTO.setType(this.userType);
        userInfoDTO.setDescription(this.groupDescription);
        userInfoDTO.setAvatar(this.avatar);
        userInfoDTO.setQrCode(this.qrCode);
        userInfoDTO.setMobile(this.mobile);
        userInfoDTO.setTelephone(this.telephone);
        userInfoDTO.setEmail(this.email);
        userInfoDTO.setStatus(this.status);
        userInfoDTO.setDeleted(this.deleted);
        userInfoDTO.setGender(this.gender);
        userInfoDTO.setWxOpenId(this.wxOpenId);
        userInfoDTO.setWxUnionId(this.wxUnionId);
        userInfoDTO.setWxWorkOpenId(this.wxWorkOpenId);
        return userInfoDTO;
    }
}
