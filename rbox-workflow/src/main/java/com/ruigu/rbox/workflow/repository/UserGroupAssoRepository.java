package com.ruigu.rbox.workflow.repository;


import com.ruigu.rbox.workflow.model.entity.UserGroupAssoEntity;
import com.ruigu.rbox.workflow.model.request.UserGroupAssoRequest;
import com.ruigu.rbox.workflow.model.vo.UserGroupAssoVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author liqingtian
 * @date 2019/09/02 20:14
 */
public interface UserGroupAssoRepository extends JpaRepository<UserGroupAssoEntity, Integer>, JpaSpecificationExecutor<UserGroupAssoEntity> {

    /**
     * 获取用户组-用户关系信息
     *
     * @param groupId 用户组id
     * @param userId  用户id
     * @return 用户组用户关系实体
     */
    UserGroupAssoEntity findByGroupIdAndUserId(Integer groupId, Integer userId);

    /**
     * 删除用户组用户关系
     *
     * @param groupId 用户组id
     * @param userId  用户id
     */
    void deleteByGroupIdAndUserId(Integer groupId, Integer userId);

    /**
     * 查询用户组-用户关系信息
     *
     * @param req      用户组关系请求
     * @param pageable 分页请求参数
     * @return 用户组信息 （分页）
     */
    @Query(" select new com.ruigu.rbox.workflow.model.vo.UserGroupAssoVO( " +
            " u.id,u.groupId,u.userId,u.createdOn,u.createdBy,u.lastUpdatedOn,u.lastUpdatedBy,u.status " +
            " ) " +
            " from UserGroupAssoEntity u " +
            " where u.groupId = :#{#req.groupId}" +
            " and ( u.status = :#{#req.status} or :#{#req.status} is null) ")
    Page<UserGroupAssoVO> selectAllAssoPage(@Param("req") UserGroupAssoRequest req, Pageable pageable);

    /**
     * 获取用户组下所有用户id
     *
     * @param groups 用户组id
     * @return 用户id列表
     */
    @Query(value = "select a.user_id from user_group_asso  a where a.group_id in (:groups)", nativeQuery = true)
    List<Integer> selectAllUserByGroupsInt(@Param("groups") List<Integer> groups);
}
