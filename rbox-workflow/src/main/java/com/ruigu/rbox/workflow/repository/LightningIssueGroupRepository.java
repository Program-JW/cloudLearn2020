package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.LightningIssueGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/01/15 20:02
 */
public interface LightningIssueGroupRepository extends JpaRepository<LightningIssueGroupEntity, Integer>, JpaSpecificationExecutor<LightningIssueGroupEntity> {
    /**
     * 根据问题id查询群信息
     *
     * @param issueId
     * @return
     */
    LightningIssueGroupEntity findFirstByIssueId(Integer issueId);

    /**
     * 通过问题id获取群组id
     *
     * @param issueIds 问题id列表
     * @return 群消息
     */
    List<LightningIssueGroupEntity> findAllByIssueIdIn(List<Integer> issueIds);

    /**
     * 通过问题id获取群组id
     *
     * @param issueId 问题id
     * @return 群消息
     */
    LightningIssueGroupEntity findByIssueId(Integer issueId);

    /**
     * 通过群组id获取信息
     *
     * @param groupId 群组id
     * @return 群组信息
     */
    LightningIssueGroupEntity findByGroupId(String groupId);
}
