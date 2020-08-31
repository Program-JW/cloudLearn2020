package com.ruigu.rbox.workflow.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author dong jing xi
 * @date 2019/9/9 17:25
 **/
@Data
public class UserGroupLeaderVO {

    private Integer userId;
    private List<Integer> groupIds;
    private List<Integer> leaderIds;
    private Integer isDevGroup;

}
