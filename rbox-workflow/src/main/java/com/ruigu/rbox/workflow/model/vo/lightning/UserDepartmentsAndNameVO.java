package com.ruigu.rbox.workflow.model.vo.lightning;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yuanlin
 * @date 2020-01-09 10:52
 */
@ApiModel(description = "用户所在一级部门，二级部门，姓名VO")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDepartmentsAndNameVO {
    @ApiModelProperty(name = "用户id", required = false, value = "用户id")
    private Integer userId;
    @ApiModelProperty(name = "昵称", required = false, value = "昵称")
    private String nickname;
    @ApiModelProperty(name = "一级部门", required = false, value = "一级部门")
    private String firstLevelDepartment;
    @ApiModelProperty(name = "二级部门", required = false, value = "二级部门")
    private String secondaryLevelDepartment;

    private Integer departmentId;
    private Integer departmentParentId;
    private String departmentName;

    public UserDepartmentsAndNameVO(Integer userId, String nickname, Integer departmentId, Integer departmentParentId, String departmentName) {
        this.userId = userId;
        this.nickname = nickname;
        this.departmentId = departmentId;
        this.departmentParentId = departmentParentId;
        this.departmentName = departmentName;
    }
}
