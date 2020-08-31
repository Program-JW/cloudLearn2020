package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.dto.UserGroupSimpleDTO;

import java.util.Collection;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/07 10:22
 */
public interface CacheManageService {

    /***
     * 初始化用户信息
     */
    void initUserInfo();


    /**
     * 初始化用户-部门信息
     */
    void initUserGroupInfo();

    /**
     * 查询用户信息
     */
    Collection<PassportUserInfoDTO> queryCacheUserInfo(Collection<Integer> userIds);

    /**
     * 查询用户-部门信息
     */
    Collection<UserGroupSimpleDTO> queryCacheUserAndGroupInfo(Collection<Integer> userIds);

    /**
     * 删除用户信息
     */
    void removeUserInfo(Collection<Integer> userIds);

    /**
     * 删除用户-部门信息
     */
    void removeUserAndGroupInfo(Collection<Integer> userIds);
}
