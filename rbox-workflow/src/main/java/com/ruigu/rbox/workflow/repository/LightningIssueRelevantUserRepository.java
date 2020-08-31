package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.LightningIssueRelevantUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author caojinghong
 * @date 2020/01/07 10:47
 */
public interface LightningIssueRelevantUserRepository extends JpaRepository<LightningIssueRelevantUserEntity, Integer>, JpaSpecificationExecutor<LightningIssueRelevantUserEntity> {
    /**
     * 根据问题Id查询所有成员
     *
     * @param issueId
     * @return
     */
    List<LightningIssueRelevantUserEntity> findAllByIssueId(Integer issueId);

    /**
     * 根据问题id和成员id寻找是否存在
     *
     * @param issueId 问题id
     * @param userId  用户id
     * @return 记录
     */
    LightningIssueRelevantUserEntity findByIssueIdAndUserId(Integer issueId, Integer userId);

    /**
     * 通过群组id获取群成员
     *
     * @param groupId 群组id
     * @return 群组成员
     */
    @Query("select u from LightningIssueGroupEntity g " +
            " left join LightningIssueRelevantUserEntity u on u.issueId = g.issueId " +
            " where g.groupId = :groupId ")
    List<LightningIssueRelevantUserEntity> getGroupUserListByGroupId(@Param("groupId") String groupId);
}
