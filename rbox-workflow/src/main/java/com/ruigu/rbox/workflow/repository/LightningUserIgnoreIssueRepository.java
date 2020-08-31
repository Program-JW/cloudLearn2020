package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.LightningUserIgnoreIssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * @author caojinghong
 * @date 2020/03/06 17:05
 */
public interface LightningUserIgnoreIssueRepository extends JpaRepository<LightningUserIgnoreIssueEntity, Integer>, JpaSpecificationExecutor<LightningUserIgnoreIssueEntity> {
    /**
     * 判断用户是否删除过该问题
     * @param issueId 问题id
     * @param userId 用户id
     * @return Optional<LightningUserIgnoreIssueEntity>
     */
    LightningUserIgnoreIssueEntity findFirstByIssueIdAndUserId(Integer issueId, Integer userId);
}
