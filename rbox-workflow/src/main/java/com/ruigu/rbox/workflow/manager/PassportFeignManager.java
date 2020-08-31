package com.ruigu.rbox.workflow.manager;

import com.ruigu.rbox.workflow.model.dto.*;
import com.ruigu.rbox.workflow.model.request.SearchGroupRequest;
import com.ruigu.rbox.workflow.model.vo.UserExtraRelationshipVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2020/03/13 15:29
 */
public interface PassportFeignManager {

    /**
     * 获取上级领导m
     *
     * @param userId 用户id
     * @return 上级领导信息
     */
    List<PassportUserInfoDTO> getSuperiorLeader(Integer userId);

    /**
     * 获取所有领导
     *
     * @param userId 用户id
     * @return 所有领导
     */
    List<List<PassportUserInfoDTO>> getAllLeader(Integer userId);

    /**
     * 批量获取所有领导
     *
     * @param userIds 用户id列表
     * @return 所有领导
     */
    Map<Integer, List<List<PassportUserInfoDTO>>> batchGetAllLeader(Collection<Integer> userIds);

    /**
     * 用于lua初始化的用户数据
     *
     * @return 所有用户信息
     */
    List<UserInfoLuaDTO> getAllUserInfo();

    /**
     * 获取redis中的用户信息
     */
    List<PassportUserInfoDTO> getAllRedisUser();

    /**
     * 获取redis中的用户信息
     */
    List<Integer> getAllRedisUserIds();

    /**
     * 获取用户信息
     */
    PassportUserInfoDTO getUserInfoFromPassport(Integer id);

    /**
     * 获取用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    PassportUserInfoDTO getUserInfoFromRedis(Integer id);

    /**
     * 获取用户信息通过redis
     *
     * @param ids 用户id
     * @return 用户信息
     */
    Map<Integer, PassportUserInfoDTO> getUserInfoMapFromRedis(Collection<Integer> ids);

    /**
     * 获取用户信息通过redis
     *
     * @param ids 用户id
     * @return 用户信息
     */
    List<PassportUserInfoDTO> getUserInfoListFromRedis(Collection<Integer> ids);

    /**
     * 获取部门信息
     *
     * @param groupId 部门id
     * @return 部门信息
     */
    PassportGroupInfoDTO getGroupInfoById(Integer groupId);

    /**
     * 搜索部门信息
     *
     * @param request 请求
     * @return 部门信息
     */
    Map<Integer, PassportGroupInfoDTO> searchGroup(SearchGroupRequest request);

    /**
     * 获取所有部门信息
     *
     * @return 部门信息
     */
    List<PassportGroupInfoDTO> getAllGroupInfo();

    /**
     * 根据角色code获取用户信息
     *
     * @param roleCode 角色code
     * @return 用户信息
     */
    List<PassportUserInfoDTO> getListUserByRoleCode(String roleCode);

    /**
     * 通过微信名称获取id
     *
     * @param wxUserId 微信用户id
     * @return 用户信息
     */
    PassportUserInfoDTO getUserByWxUserId(String wxUserId);

    /**
     * 获取用户第三方关联用户信息
     *
     * @param userId 用户id
     * @return 关联用户信息
     */
    UserExtraRelationshipVO getExtraUserInfoByUserId(Integer userId);
}
