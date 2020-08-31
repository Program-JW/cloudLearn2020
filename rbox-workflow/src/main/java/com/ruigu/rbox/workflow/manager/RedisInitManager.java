package com.ruigu.rbox.workflow.manager;

import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.dto.UserGroupSimpleDTO;
import com.ruigu.rbox.workflow.model.dto.UserInfoLuaDTO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2020/04/25 11:17
 */
public interface RedisInitManager {

    /**
     * 检验是否初始化信息
     *
     * @return 是否初始化 （true 已初始化 false 未初始化）
     */
    Boolean checkPassportUserInit();

    /**
     * 检验用户部门是否初始化
     *
     * @return 是否初始化 （结果同上）
     */
    boolean checkUserGroupInit();

    /**
     * 初始化 权限中心用户信息
     *
     * @param userInfoList 用户信息列表
     */
    void initPassportUserInfo(List<UserInfoLuaDTO> userInfoList);

    /**
     * 初始化 权限中心用户部门信息
     *
     * @param userGroupInfo 用户id列表
     */
    void initUserGroupInfo(Map<Integer, UserGroupSimpleDTO> userGroupInfo);

    /**
     * 初始化 权限中心用户信息
     *
     * @param userInfoList 用户信息列表
     */
    void asyncInitPassportUserInfo(List<PassportUserInfoDTO> userInfoList);

    /**
     * 初始化 权限中心用户信息
     *
     * @param data 用户部门信息列表
     */
    void asyncInitUserGROUPInfo(Map<Integer, UserGroupSimpleDTO> data);

    /**
     * 批量获取 用户信息 （lua）
     *
     * @param ids id列表
     * @return 用户信息
     */
    // Map<Integer, PassportUserInfoDTO> batchGetPassportUserInfo(Collection<Integer> ids);

    /**
     * 批量获取
     *
     * @param <K>    k
     * @param <V>    v
     * @param ids    id
     * @param prefix key前缀
     * @return map
     */
    <K, V> Map<K, V> batchGet(Collection<Integer> ids, String prefix, Class<K> keyClazz, Class<V> valueClazz);

    /**
     * 根据微信更新缓存值
     *
     * @param userInfo 用户信息
     */
    void updatePassportInfo(PassportUserInfoDTO userInfo);

    /**
     * 删除人员部门缓存
     *
     * @param userId 用户id
     */
    void removeUserGroupInfo(Integer userId);

    /**
     * remove
     */
    void removeKeys(Collection<String> keys);

}
