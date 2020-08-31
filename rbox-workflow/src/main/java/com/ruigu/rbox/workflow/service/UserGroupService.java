package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.entity.UserGroupAssoEntity;
import com.ruigu.rbox.workflow.model.entity.UserGroupEntity;
import com.ruigu.rbox.workflow.model.request.SelectRequest;
import com.ruigu.rbox.workflow.model.request.UserGroupAssoRequest;
import com.ruigu.rbox.workflow.model.request.UserGroupRequest;
import com.ruigu.rbox.workflow.model.vo.SelectOption;
import com.ruigu.rbox.workflow.model.vo.UserGroupAssoVO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author liqingtian
 * @date 2019/09/02 20:01
 */
public interface UserGroupService {

    /**
     * 创建用户组
     *
     * @param userGroupEntity 用户组信息实体
     */
    void createUserGroup(UserGroupEntity userGroupEntity);

    /**
     * 修改用户组
     *
     * @param userGroupEntity 用户组信息实体
     */
    void updateUserGroup(UserGroupEntity userGroupEntity);

    /**
     * 移除用户组
     *
     * @param groupId 用户组ID
     */
    void removeUserGroup(Integer groupId);

    /**
     * 获取用户组数据
     *
     * @param req 用户组信息筛选条件
     * @return Page<UserGroupEntity>
     */
    Page<UserGroupEntity> selectGroupPage(UserGroupRequest req);

    /**
     * 向用户组添加用户
     *
     * @param userGroupAssoRequest 添加信息实体
     */
    void addUserToGroup(UserGroupAssoRequest userGroupAssoRequest);

    /**
     * 移除用户组用户
     *
     * @param userGroupAsso 用户组与用户关系实体
     * @return ServerResponse
     */
    ServerResponse removeUserToGroup(UserGroupAssoEntity userGroupAsso);

    /**
     * 获取用户组用户数据
     *
     * @param req 用户组用户详情筛选条件
     * @return Page<UserGroupAssoVO>
     */
    Page<UserGroupAssoVO> selectGroupAssoPage(UserGroupAssoRequest req);

    /**
     * 通过用户组ID获取用户组信息
     *
     * @param groupId 用户组id
     * @return UserGroupEntity
     */
    UserGroupEntity getUserGroupById(Integer groupId);

    /**
     * 选择用户组
     *
     * @param request 参数对象
     * @return 用户组列表
     */
    List<SelectOption> selectGroups(SelectRequest request);

    /**
     * 选择用户组
     *
     * @param ids 需要置顶的组
     * @param key 模糊搜索关键字
     * @param top 至多返回top条符合条件的数据
     * @return 用户组列表
     */
    List<SelectOption> selectGroups(List<Integer> ids, String key, Integer top);

    /**
     * 根据用户组id返回所有用户id
     *
     * @param groupIds 用户组id列表
     * @return List<Integer>
     */
    List<Integer> getUserListByGroupsInt(List<String> groupIds);
}
