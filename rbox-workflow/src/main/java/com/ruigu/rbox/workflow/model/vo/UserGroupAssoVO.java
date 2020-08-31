package com.ruigu.rbox.workflow.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author liqingtian
 * @date 2019/09/03 19:21
 */
@Data
public class UserGroupAssoVO {
    private Integer id;
    private Integer groupId;
    private Integer userId;
    private String userName;
    private Date createdOn;
    private Integer createdBy;
    private Date lastUpdatedOn;
    private Integer lastUpdatedBy;
    private Integer status;

    public UserGroupAssoVO(Integer id, Integer groupId, Integer userId, Date createdOn, Integer createdBy, Date lastUpdatedOn, Integer lastUpdatedBy, Integer status) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.lastUpdatedOn = lastUpdatedOn;
        this.lastUpdatedBy = lastUpdatedBy;
        this.status = status;
    }
}
