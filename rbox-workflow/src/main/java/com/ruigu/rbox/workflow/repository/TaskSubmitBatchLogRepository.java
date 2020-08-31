package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.TaskSubmitBatchLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liqingtian
 * @date 2019/10/08 16:58
 */
public interface TaskSubmitBatchLogRepository extends JpaRepository<TaskSubmitBatchLogEntity, Integer> {

    /**
     * 更新批处理进度
     *
     * @param batchLogId 批处理日志id
     * @param success    成功数
     * @param fail       失败数
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update TaskSubmitBatchLogEntity l set l.success=:success,l.fail=:fail where l.id=:batchLogId")
    void updateBatchSuccessAndFailCount(@Param("batchLogId") Integer batchLogId, @Param("success") Integer success, @Param("fail") Integer fail);

    /**
     * 更新批处理成功失败id
     *
     * @param batchLogId 批处理日志id
     * @param successIds 成功数
     * @param failIds    失败数
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update TaskSubmitBatchLogEntity l set l.successId =:successIds,l.failId = :failIds,l.success=:success,l.fail=:fail where l.id=:batchLogId")
    void updateBatchSuccessAndFailIds(@Param("batchLogId") Integer batchLogId, @Param("successIds") String successIds, @Param("success") Integer success, @Param("failIds") String failIds, @Param("fail") Integer fail);
}
