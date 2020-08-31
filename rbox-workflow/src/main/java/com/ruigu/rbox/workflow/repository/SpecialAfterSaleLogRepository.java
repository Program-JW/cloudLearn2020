package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/11 13:00
 */
public interface SpecialAfterSaleLogRepository extends JpaRepository<SpecialAfterSaleLogEntity, Long> {

    /**
     * 查询日志列表通过applyid 和 isshow字段
     *
     * @param applyId applyid
     * @param isShow  是否显示
     * @return 日志列表
     */
    List<SpecialAfterSaleLogEntity> findAllByApplyIdAndShowOrderById(Long applyId, Integer isShow);

    /**
     * 查询日志列表通过applyId 和 动作
     *
     * @param applyId applyid
     * @param action  动作
     * @return 日志列表
     */
    List<SpecialAfterSaleLogEntity> findAllByApplyIdAndActionOrderByIdDesc(Long applyId, Integer action);

    /**
     * 根据任务id 和 创建人 查询日志
     *
     * @param taskId    task
     * @param action    动作
     * @param createdBy 创建人
     * @return 日志
     */
    SpecialAfterSaleLogEntity findByApplyIdAndTaskIdAndActionAndCreatedBy(Long applyId, String taskId, Integer action, Integer createdBy);

    /**
     * 根据任务id 和 创建人 查询日志
     *
     * @param taskId    task
     * @param action    动作
     * @return 日志
     */
    List<SpecialAfterSaleLogEntity> findByApplyIdAndTaskIdAndAction(Long applyId, String taskId, Integer action);
}
