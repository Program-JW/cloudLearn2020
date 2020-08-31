package com.ruigu.rbox.workflow.manager;

import com.ruigu.rbox.workflow.model.dto.UserGroupSimpleDTO;
import com.ruigu.rbox.workflow.model.dto.UserSearchRequestDTO;

import java.util.Collection;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2020/08/05 22:06
 */
public interface UserManager {

    /**
     * 查询用户部门信息
     *
     * @param request 请求
     * @return 用户部门信息
     */
    Map<Integer, UserGroupSimpleDTO> searchUserGroupFromApi(UserSearchRequestDTO request);

    /**
     * 通过userId获取缓存的信息
     *
     * @param userIds 请求
     * @return 用户部门信息
     */
    Map<Integer, UserGroupSimpleDTO> searchUserGroupFromCache(Collection<Integer> userIds);

    /**
     * 通过userId获取缓存的信息
     *
     * @param userId 请求
     * @return 用户部门信息
     */
    UserGroupSimpleDTO searchUserGroupFromCache(Integer userId);

}
