package com.ruigu.rbox.workflow.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/01/10 14:51
 */
@ApiModel(description = "部门人员接口VO")
@Data
public class GroupAndUserVO {

    @ApiModelProperty(name = "名称", required = false, value = "名称")
    private String label;

    @ApiModelProperty(name = "职位", required = false, value = "职位")
    private String position;

    @ApiModelProperty(name = "员工所在部门描述", required = false, value = "员工所在部门描述")
    private List<String> departmentDescriptionList;
    @ApiModelProperty(name = "员工所在部门Ids", required = false, value = "员工所在部门Ids")
    private List<Integer> departmentIds;

    @ApiModelProperty(name = "员工的头像", required = false, value = "员工的头像")
    private String avatar;

    @ApiModelProperty(name = "id", required = false, value = "id")
    private Integer value;

    @ApiModelProperty(name = "类型", required = false, value = "1代表部门，2代表人")
    private Integer type;

    private List<GroupAndUserVO> children;

    private String userCode;
    private String workWxUserId;
    private String username;
    private String nickname;
    private String cnName;
    private String enName;
    private String description;
    private String qrCode;
    private String mobile;
    private String telephone;
    private String email;
    private Integer gender;

    public GroupAndUserVO() {
    }

    public GroupAndUserVO(String label, Integer value, Integer type) {
        this.label = label;
        this.value = value;
        this.type = type;
    }

    public GroupAndUserVO(String label, Integer value) {
        this.label = label;
        this.value = value;
    }

    public GroupAndUserVO(String label, String position, String avatar, Integer value, Integer type) {
        this.label = label;
        this.position = position;
        this.avatar = avatar;
        this.value = value;
        this.type = type;
    }
}
