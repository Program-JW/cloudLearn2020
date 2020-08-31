package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.OperationLogEntity;
import com.ruigu.rbox.workflow.model.request.OperationLogRequest;
import com.ruigu.rbox.workflow.model.vo.OperationLogVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author liqingtian
 * @date 2019/09/02 19:54
 */
public interface OperationLogRepository extends JpaRepository<OperationLogEntity, Integer> {
    /**
     * 通过实例Id,获取操作历史中对该实例的操作
     *
     * @param instanceId 实例id
     * @return List<String>
     */
    @Query(value = "SELECT log.content FROM OperationLogEntity log where log.instanceId=?1 and log.status=1 order BY log.createdOn")
    List<String> selectProcessByInstanceId(String instanceId);

    /**
     * 通过实例id和状态获取日志
     *
     * @param instanceId 实例id
     * @param status     状态
     * @return List<OperationLogEntity>
     */
    List<OperationLogEntity> findByInstanceIdAndStatus(String instanceId, Integer status);

    /**
     * 查找流程实例信息
     *
     * @param taskIds    任务ids
     * @param status     状态
     * @param instanceId 实例id
     * @return List<OperationLogEntity>
     */
    @Query(" select l  " +
            " from OperationLogEntity l " +
            " left join WorkflowDefinitionEntity d on l.definitionId = d.id " +
            " left join TaskEntity t on l.taskId = t.id " +
            " where (l.instanceId = :instanceId or :instanceId is null ) " +
            " and (l.taskId not in (:taskIds) or l.taskId is null) " +
            " and (l.status = :status ) ")
    List<OperationLogEntity> findAllByInstanceIdAndStatusAndTaskIdNotIn(@Param("instanceId") String instanceId, @Param("status") Integer status, @Param("taskIds") List<String> taskIds);

    /**
     * mp采购流程专用 查询流程mp更改状态日志
     *
     * @param instanceId 实例id
     * @param status     状态
     * @param event      事件
     * @return 日志记录
     */
    OperationLogEntity findByInstanceIdAndStatusAndEvent(String instanceId, Integer status, String event);

    /**
     * 筛选
     *
     * @param req      请求参数
     * @param pageable 分页参数
     * @return 日志列表
     */
    @Query(" select new com.ruigu.rbox.workflow.model.vo.OperationLogVO(l.id, " +
            " l.content, " +
            " l.status, " +
            " l.definitionId, " +
            " d.name, " +
            " l.instanceId, " +
            " l.taskId, " +
            " t.name, " +
            " l.createdOn, " +
            " l.createdBy, " +
            " l.lastUpdatedBy, " +
            " l.lastUpdatedOn ) " +
            " from OperationLogEntity l " +
            " left join WorkflowDefinitionEntity d on l.definitionId = d.id " +
            " left join TaskEntity t on l.taskId = t.id " +
            " where (l.instanceId = :#{#req.instanceId} or :#{#req.instanceId} is null ) " +
            " and (l.definitionId = :#{#req.definitionId} or :#{#req.definitionId} is null ) " +
            " and (l.taskId = :#{#req.taskId} or :#{#req.taskId} is null ) " +
            " and (l.createdBy = :#{#req.createdBy} or :#{#req.createdBy} is null ) " +
            " and (l.content like :#{#req.content} or :#{#req.content} is null ) " +
            " and (l.status = :#{#req.status} or :#{#req.status} is null ) " +
            " and (l.createdOn >= :#{#req.begin} or :#{#req.begin} is null ) " +
            " and (l.createdOn <= :#{#req.end} or :#{#req.end} is null ) " +
            " order by l.createdOn desc ")
    Page<OperationLogVO> selectAllLogs(@Param("req") OperationLogRequest req, Pageable pageable);

    /**
     * 获取流程的所有日志
     *
     * @param instanceId 流程id
     * @return 该流程所记录日志
     */
    List<OperationLogEntity> findAllByInstanceId(String instanceId);

    /**
     * 获取流程的所有日志
     *
     * @param instanceId 流程id
     * @param event      事件
     * @return 该流程所记录日志
     */
    List<OperationLogEntity> findAllByInstanceIdAndEvent(String instanceId, String event);
}
