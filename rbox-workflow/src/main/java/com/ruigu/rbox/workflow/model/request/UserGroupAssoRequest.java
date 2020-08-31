package com.ruigu.rbox.workflow.model.request;

import lombok.Setter;

import java.util.List;

/**
 * @author liqingtian
 * @date 2019/09/02 21:14
 */
@Setter
public class UserGroupAssoRequest {

    private Integer pageNum;

    private Integer pageSize;

    private Integer status;

    private Integer groupId;

    private List<Integer> userIds;

    public Integer getPageNum() {
        if (pageNum == null || pageNum < 1) {
            return 0;
        }
        return pageNum - 1;
    }

    public Integer getPageSize() {
        if (pageSize == null || pageSize < 1) {
            return 20;
        }
        return pageSize;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }
}
