package com.ruigu.rbox.workflow.model.request;

import lombok.Data;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/07 22:28
 */
@Data
public class UserGroupSearchReq {

    private String userIdList;
    private Integer userStatus;
    private Integer userDeleted;
    private Integer groupStatus;
    private Integer groupDeleted;
}
