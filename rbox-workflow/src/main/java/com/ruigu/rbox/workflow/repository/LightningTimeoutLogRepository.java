package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.LightningTimeoutLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/02/26 13:22
 */
public interface LightningTimeoutLogRepository extends JpaRepository<LightningTimeoutLogEntity, Integer> {

    /**
     * 根据问题id获取问题超时记录
     *
     * @param iterable id列表
     * @return 超时日志
     */
    List<LightningTimeoutLogEntity> findAllByIssueIdIn(Iterable<Integer> iterable);
}
