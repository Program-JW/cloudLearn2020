package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.dto.InstanceTaskInfoDTO;
import com.ruigu.rbox.workflow.model.entity.TaskEntity;
import com.ruigu.rbox.workflow.model.vo.TaskVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author alan.zhao
 */
public interface TaskRepository extends JpaRepository<TaskEntity, String>, JpaSpecificationExecutor<TaskEntity> {

    /**
     * 更新任务为开始处理状态
     *
     * @param taskId    任务id
     * @param beginTime 开始时间
     * @return 改变行数
     */
    @Modifying
    @Query("update TaskEntity " +
            " set status = 1,beginTime = :beginTime " +
            " where id = :taskId ")
    @Transactional(rollbackFor = Exception.class)
    int updateBeginStatus(@Param("taskId") String taskId, @Param("beginTime") Date beginTime);

    /**
     * 获取该实例所有任务
     *
     * @param instanceId 实例id
     * @return List<TaskEntity>
     */
    @Query(" select new com.ruigu.rbox.workflow.model.vo.TaskVO(t.id,t.name,t.status,t.submitTime,t.submitBy,t.createdOn) " +
            " from TaskEntity t where t.instanceId=:instanceId")
    List<TaskVO> selectAllTaskByInstanceId(@Param("instanceId") String instanceId);

    /**
     * 获取所有的任务通过实例id
     *
     * @param instanceId 实例id
     * @return List<String>
     */
    @Query(" select t.id from TaskEntity t where t.instanceId = :instanceId ")
    List<String> selectTaskIdsByInstanceId(@Param("instanceId") String instanceId);

    /**
     * 获取通知内容
     *
     * @param taskId 任务id
     * @return TaskEntity
     */
    @Query(" select new com.ruigu.rbox.workflow.model.entity.TaskEntity(t.id,t.name,t.instanceId,t.status,t.submitBy,t.submitTime,t.noticeType,t.noticeContent) from TaskEntity t where t.id = :taskId")
    TaskEntity selectNoticeContentByTaskId(@Param("taskId") String taskId);

    /**
     * 获取实例中未完成任务
     *
     * @param instanceId 实例id
     * @return TaskEntity
     */
    @Query(value = " select new com.ruigu.rbox.workflow.model.entity.TaskEntity(t.id,t.instanceId,t.data) " +
            " from TaskEntity t where t.instanceId = :instanceId " +
            " and t.status in (0,1) ")
    List<TaskEntity> selectIncompleteTaskByInstanceId(@Param("instanceId") String instanceId);

    /**
     * 获取实例中未完成任务
     *
     * @param instanceId 实例id
     * @param status     任务状态
     * @return TaskEntity
     */
    TaskEntity findByInstanceIdAndStatus(String instanceId, Integer status);

    /**
     * 获取当前任务信息通过businessKey 和 流程定义key
     *
     * @param definitionKey 流程定义key
     * @param businessKey   业务主键key
     * @return 当前任务信息
     */
    @Query("select t from TaskEntity t " +
            " left join WorkflowInstanceEntity i on t.instanceId = i.id " +
            " where i.definitionCode=:definitionKey " +
            " and i.businessKey = :businessKey " +
            " and t.status in (0,1) ")
    TaskEntity findCurrentTaskByDefinitionKeyAndBusinessKey(@Param("definitionKey") String definitionKey, @Param("businessKey") String businessKey);

    /**
     * 获取当前任务信息通过流程ID
     *
     * @param instanceId 流程ID
     * @return 当前任务信息
     */
    @Query("select t from TaskEntity t " +
            " left join WorkflowInstanceEntity i on t.instanceId = i.id " +
            " where i.id = :instanceId and i.status in (0,1) and t.status in (0,1) ")
    TaskEntity findCurrentTaskByInstanceId(@Param("instanceId") String instanceId);

    /**
     * 获取实例中未完成任务
     *
     * @param instanceIds 实例id列表
     * @return TaskEntity
     */
    @Query(value = " select t " +
            " from TaskEntity t where t.instanceId in (:instanceId) " +
            " and t.status in (0,1) ")
    List<TaskEntity> selectIncompleteTaskByInstanceIds(@Param("instanceId") List<String> instanceIds);

    /**
     * 获取未完成任务通过流程定义key
     *
     * @param definitionKey 流程
     * @return 未完成任务
     */
    @Query("select t " +
            " from TaskEntity t " +
            " left join WorkflowInstanceEntity i on t.instanceId = i.id " +
            " left join WorkflowDefinitionEntity d on i.definitionId = d.id " +
            " where d.initialCode = :definitionKey and t.status in (0,1) ")
    List<TaskEntity> selectIncompleteTaskByDefinitionKey(@Param("definitionKey") String definitionKey);

    /**
     * 获取实例中未完成任务
     *
     * @param instanceId 实例id
     * @return TaskEntity
     */
    @Query(value = " select new com.ruigu.rbox.workflow.model.entity.TaskEntity(t.id,t.instanceId,t.data) " +
            " from TaskEntity t where t.instanceId = :instanceId " +
            " and t.status in (2,3,4) ")
    List<TaskEntity> selectCompleteTaskByInstanceId(@Param("instanceId") String instanceId);

    /**
     * 修改任务状态为处理
     *
     * @param taskIds 任务id列表
     * @param status  状态
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update TaskEntity t set t.status=:status where t.id in (:ids)")
    void updateProcessingStatus(@Param("ids") List<String> taskIds, @Param("status") Integer status);

    /**
     * 修改任务状态为作废状态
     *
     * @param instanceId 流程实例id
     * @param status     作废状态
     * @return 影响行数
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update TaskEntity t set t.status=:status where t.instanceId=:instanceId")
    int updateRevokeStatus(@Param("instanceId") String instanceId, @Param("status") Integer status);

    /**
     * 批量修改任务状态为作废状态
     *
     * @param instanceIds 流程实例ids
     * @param status      作废状态
     * @return 影响行数
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update TaskEntity t set t.status=:status where t.instanceId in (:instanceIds)")
    int updateRevokeStatus(@Param("instanceIds") List<String> instanceIds, @Param("status") Integer status);

    /**
     * 获取所有的任务
     *
     * @param instanceId 实例id
     * @return 任务列表
     */
    List<TaskEntity> findAllByInstanceId(String instanceId);


    /**
     * 闪电链 - 通过businessKey 和 userId 获取当前任务id
     *
     * @param definitionKey 流程定义key
     * @param userId        候选人id
     * @param businessKeys  业务主键列表
     * @return 当前任务id
     */
    @Query("select new com.ruigu.rbox.workflow.model.dto.InstanceTaskInfoDTO(i.businessKey,t.id) " +
            " from WorkflowInstanceEntity i " +
            " left join TaskEntity t on i.id = t.instanceId " +
            " where i.definitionCode=:definitionKey " +
            " and i.businessKey in (:businessKeys) " +
            " and t.status in (0,1) " +
            " and t.candidateUsers = :userId ")
    List<InstanceTaskInfoDTO> findCurrentTaskIdByBusinessKeyIn(@Param("definitionKey") String definitionKey,
                                                               @Param("businessKeys") List<String> businessKeys,
                                                               @Param("userId") String userId);

}
