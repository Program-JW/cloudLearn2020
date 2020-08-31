package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.UserGroupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author liqingtian
 * @date 2019/09/02 20:13
 */
public interface UserGroupRepository extends JpaRepository<UserGroupEntity, Integer>, JpaSpecificationExecutor<UserGroupEntity> {

    /**
     * 通过id和状态查询用户组信息
     *
     * @param id     用户组id
     * @param status 状态
     * @return 用户组信息
     */
    UserGroupEntity findByIdAndStatus(Integer id, Integer status);

    /**
     * 通过用户组id和状态筛选用户组信息
     *
     * @param ids    用户组id
     * @param status 状态
     * @return 用户组信息（分页）
     */
    @Query("select e from UserGroupEntity e where e.id in (?1) and e.status=?2")
    List<UserGroupEntity> findByIdInAndStatus(Iterable<Integer> ids, Integer status);

    /**
     * 通过组名和状态筛选用户组信息
     *
     * @param key      用户组名称
     * @param status   状态
     * @param pageable 分页请求参数
     * @return 用户组信息（分页）
     */
    @Query("select e from UserGroupEntity e where e.name like concat('%',?1,'%') and e.status=?2")
    Page<UserGroupEntity> findByNameAndStatus(String key, Integer status, Pageable pageable);
}
