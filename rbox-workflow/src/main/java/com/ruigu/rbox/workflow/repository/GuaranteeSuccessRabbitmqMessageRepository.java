package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.ReliableMqLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/07/21 10:52
 */
public interface GuaranteeSuccessRabbitmqMessageRepository extends JpaRepository<ReliableMqLogEntity, Long> {

    /**
     * 查询未发送成功mq消息
     *
     * @param statusList 状态列表
     * @return 未成功mq消息记录
     */
    @Query(" select r " +
            " from ReliableMqLogEntity r " +
            " where r.messageStatus in (:statusList) and r.currentRetryTimes < r.maxRetryTimes")
    List<ReliableMqLogEntity> findAllNeedRetryRecord(List<Integer> statusList);
}
