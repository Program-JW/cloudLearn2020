package com.ruigu.rbox.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.List;

/**
 * @author yuanLin
 * @date 2020-05-25 15:07
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@FieldNameConstants
public class UserGroupSimpleDTO {
    private Integer userId;
    private Integer userStatus;
    private Integer userDeleted;
    private String nickname;
    private String position;
    private String userCode;

    /**
     * 部门信息
     */
    private List<GroupInfoVO> groups;

    @FieldNameConstants
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class GroupInfoVO {
        private Integer userId;
        private Integer groupId;
        private String groupName;
        private String groupDecs;
        private Integer groupStatus;
        private Integer groupDeleted;
    }
}


