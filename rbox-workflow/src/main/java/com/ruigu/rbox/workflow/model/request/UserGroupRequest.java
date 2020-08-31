package com.ruigu.rbox.workflow.model.request;

import lombok.Setter;

/**
 * @author liqingtian
 * @date 2019/09/02 20:05
 */
@Setter
public class UserGroupRequest {

    private Integer pageNum;

    private Integer pageSize;

    private String name;

    private Integer status;

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

    public String getName() {
        return name;
    }

    public Integer getStatus() {
        return status;
    }
}
