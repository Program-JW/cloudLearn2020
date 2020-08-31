package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.LightningIssueEvaluationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author caojinghong
 * @date 2019/12/27 20:20
 */
public interface LightningIssueEvaluationRepository extends JpaRepository<LightningIssueEvaluationEntity, Integer>, JpaSpecificationExecutor<LightningIssueEvaluationEntity> {
    /**
     * 根据问题id判断是否评价过
     *
     * @param issueId 问题id
     * @param instanceId
     * @return 记录
     */
    LightningIssueEvaluationEntity findByIssueIdAndInstanceId(Integer issueId, String instanceId);
}
