package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.WorkflowInstanceEntity;
import com.ruigu.rbox.workflow.model.entity.WorkflowInstanceWithTaskEntity;
import com.ruigu.rbox.workflow.model.request.SearchInstanceRequest;
import com.ruigu.rbox.workflow.model.vo.TaskVO;
import com.ruigu.rbox.workflow.model.vo.WorkflowInstanceVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author alan.zhao
 */
public interface WorkflowInstanceRepository extends JpaRepository<WorkflowInstanceEntity, String>, JpaSpecificationExecutor<WorkflowInstanceEntity> {

    /**
     * 通过id获取流程实例
     *
     * @param id 流程实例ID
     * @return WorkflowInstanceEntity
     */
    WorkflowInstanceEntity findTopById(String id);

    /**
     * 获取实例列表 (可选择候选人 若 需要获取所有则候选人填空)
     *
     * @param candidateId 候选人id
     * @return List<TaskVO>
     */
    @Query(value = "select new com.ruigu.rbox.workflow.model.vo.TaskVO(t.instanceId,i.name) " +
            " from TaskEntity t left join WorkflowInstanceEntity i on t.instanceId = i.id " +
            " where (t.candidateUsers = :candidateId or t.candidateUsers is null ) ")
    List<TaskVO> selectInstanceList(@Param("candidateId") String candidateId);

    /**
     * 获取所有实例列表
     *
     * @param req      查找参数
     * @param pageable 分页参数
     * @return Page<WorkflowInstanceVO>
     */
    @Query(value = "select new com.ruigu.rbox.workflow.model.vo.WorkflowInstanceVO( " +
            " i.id," +
            " i.name, " +
            " i.businessKey, " +
            " i.businessUrl, " +
            " i.mobileBusinessUrl, " +
            " i.pcBusinessUrl, " +
            " i.definitionId, " +
            " i.definitionCode, " +
            " d.name, " +
            " i.definitionVersion, " +
            " i.startTime, " +
            " i.endTime, " +
            " i.deleteReason, " +
            " i.ownerId, " +
            " i.createdBy, " +
            " i.createdOn, " +
            " i.lastUpdatedBy, " +
            " i.lastUpdatedOn, " +
            " i.status ) " +
            " from WorkflowInstanceEntity i left join WorkflowDefinitionEntity d " +
            " on i.definitionId = d.id " +
            " where " +
            " (i.name like concat('%',:#{#req.name},'%') or :#{#req.name} is null ) " +
            " and (i.definitionId = :#{#req.definitionId} or :#{#req.definitionId} is null ) " +
            " and (i.status = :#{#req.status} or :#{#req.status} is null ) " +
            " and (i.createdOn >= :#{#req.begin} or :#{#req.begin} is null ) " +
            " and (i.createdOn <= :#{#req.end} or :#{#req.end} is null ) " +
            " order by i.createdOn desc ")
    Page<WorkflowInstanceVO> selectInstances(@Param("req") SearchInstanceRequest req, Pageable pageable);

    /**
     * 获取自己负责的实例列表
     *
     * @param ownerId  负责人id
     * @param req      查找参数
     * @param pageable 分页参数
     * @return Page<WorkflowInstanceVO>
     */
    @Query(value = "select new com.ruigu.rbox.workflow.model.vo.WorkflowInstanceVO( " +
            " i.id," +
            " i.name, " +
            " i.businessKey, " +
            " i.businessUrl, " +
            " i.mobileBusinessUrl, " +
            " i.pcBusinessUrl, " +
            " i.definitionId, " +
            " i.definitionCode, " +
            " d.name, " +
            " i.definitionVersion, " +
            " i.startTime, " +
            " i.endTime, " +
            " i.deleteReason, " +
            " i.ownerId, " +
            " i.createdBy, " +
            " i.createdOn, " +
            " i.lastUpdatedBy, " +
            " i.lastUpdatedOn, " +
            " i.status ) " +
            " from WorkflowInstanceEntity i left join WorkflowDefinitionEntity d " +
            " on i.definitionId = d.id " +
            " where i.ownerId = :ownerId " +
            " and (i.name like concat('%',:#{#req.name},'%') or :#{#req.name} is null ) " +
            " and (i.definitionId = :#{#req.definitionId} or :#{#req.definitionId} is null ) " +
            " and (i.status = :#{#req.status} or :#{#req.status} is null ) " +
            " and (i.createdOn >= :#{#req.begin} or :#{#req.begin} is null ) " +
            " and (i.createdOn <= :#{#req.end} or :#{#req.end} is null ) " +
            " order by i.createdOn desc ")
    Page<WorkflowInstanceVO> selectOwnerInstances(@Param("req") SearchInstanceRequest req, @Param("ownerId") Long ownerId, Pageable pageable);

    /**
     * 获取自己创建的实例列表
     *
     * @param createId 负责人id
     * @param request  查找参数
     * @param pageable 分页参数
     * @return Page<WorkflowInstanceVO>
     */
    @Query(value = "select new com.ruigu.rbox.workflow.model.vo.WorkflowInstanceVO( " +
            " i.id," +
            " i.name, " +
            " i.businessKey, " +
            " i.businessUrl, " +
            " i.mobileBusinessUrl, " +
            " i.pcBusinessUrl, " +
            " i.definitionId, " +
            " i.definitionCode, " +
            " d.name, " +
            " i.definitionVersion, " +
            " i.startTime, " +
            " i.endTime, " +
            " i.deleteReason, " +
            " i.ownerId, " +
            " i.createdBy, " +
            " i.createdOn, " +
            " i.lastUpdatedBy, " +
            " i.lastUpdatedOn, " +
            " i.status ) " +
            " from WorkflowInstanceEntity i left join WorkflowDefinitionEntity d " +
            " on i.definitionId = d.id " +
            " where i.createdBy = :createId " +
            " and (i.name like concat('%',:#{#req.name},'%') or :#{#req.name} is null ) " +
            " and (i.definitionId = :#{#req.definitionId} or :#{#req.definitionId} is null ) " +
            " and (i.status = :#{#req.status} or :#{#req.status} is null ) " +
            " and (i.createdOn >= :#{#req.begin} or :#{#req.begin} is null ) " +
            " and (i.createdOn <= :#{#req.end} or :#{#req.end} is null ) " +
            " order by i.createdOn desc ")
    Page<WorkflowInstanceVO> selectCreateInstances(@Param("req") SearchInstanceRequest request, @Param("createId") Long createId, Pageable pageable);

    /**
     * 通过id获取流程实例详情
     *
     * @param id 查找参数
     * @return WorkflowInstanceVO
     */
    @Query(value = "select new com.ruigu.rbox.workflow.model.vo.WorkflowInstanceVO( " +
            " i.id," +
            " i.name, " +
            " i.businessKey, " +
            " i.businessUrl, " +
            " i.mobileBusinessUrl, " +
            " i.pcBusinessUrl, " +
            " i.definitionId, " +
            " i.definitionCode, " +
            " d.name, " +
            " i.definitionVersion, " +
            " i.startTime, " +
            " i.endTime, " +
            " i.deleteReason, " +
            " i.ownerId, " +
            " i.createdBy, " +
            " i.createdOn, " +
            " i.lastUpdatedBy, " +
            " i.lastUpdatedOn, " +
            " i.status ) " +
            " from WorkflowInstanceEntity i left join WorkflowDefinitionEntity d " +
            " on i.definitionId = d.id " +
            " where i.id = :id ")
    WorkflowInstanceVO selectInstanceById(@Param("id") String id);

    /**
     * 通过任务ID获取所属流程定义实例详情
     *
     * @param id 查找参数
     * @return WorkflowInstanceVO
     */
    @Query(value = "select new com.ruigu.rbox.workflow.model.vo.WorkflowInstanceVO( " +
            " i.id," +
            " i.name, " +
            " i.businessKey, " +
            " i.businessUrl, " +
            " i.mobileBusinessUrl, " +
            " i.pcBusinessUrl, " +
            " i.definitionId, " +
            " i.definitionCode, " +
            " d.name, " +
            " i.definitionVersion, " +
            " i.startTime, " +
            " i.endTime, " +
            " i.deleteReason, " +
            " i.ownerId, " +
            " i.createdBy, " +
            " i.createdOn, " +
            " i.lastUpdatedBy, " +
            " i.lastUpdatedOn, " +
            " i.status ) " +
            " from WorkflowDefinitionEntity d left join WorkflowInstanceEntity i " +
            " on d.id = i.definitionId " +
            " left join TaskEntity t " +
            " on i.id = t.instanceId " +
            " where t.id = :id ")
    WorkflowInstanceVO selectInstanceVOByTaskId(@Param("id") String id);

    /**
     * 通过任务id获取所属实例的信息0
     *
     * @param taskId 任务id
     * @return WorkflowInstanceEntity
     */
    @Query(" select i from WorkflowInstanceEntity i , TaskEntity t " +
            " where i.id = t.instanceId and t.id = :taskId ")
    WorkflowInstanceEntity selectInstanceByTaskId(@Param("taskId") String taskId);

    /**
     * 通过businessKey获取流程实例
     *
     * @param businessKey 业务主键
     * @return WorkflowInstanceEntity
     */
    WorkflowInstanceEntity findTopByBusinessKey(String businessKey);

    /**
     * 查找正在运行的流程通过businessKey
     *
     * @param businessKey businessKey
     * @param statusList  状态列表
     * @return 实例信息
     */
    WorkflowInstanceEntity findByBusinessKeyAndStatusIn(String businessKey, List<Integer> statusList);

    /**
     * 通过businessKey 和 流程状态 查询实例
     *
     * @param businessKeys 业务流程key
     * @param status       状态
     * @return 没有作废的实例
     */
    List<WorkflowInstanceEntity> findAllByBusinessKeyInAndStatusNot(List<String> businessKeys, Integer status);

    /**
     * 通过businessKey 和 流程状态 和 创建人id 查询实例
     *
     * @param businessKeys 业务流程key
     * @param status       状态
     * @param userId       用户id
     * @return 没有作废的实例
     */
    List<WorkflowInstanceEntity> findAllByBusinessKeyInAndStatusNotAndCreatedBy(List<String> businessKeys, Integer status, Long userId);

    /**
     * 通过流程ID 和 流程状态 和 创建人id 查询实例
     *
     * @param instanceIds 流程ID列表
     * @param status      状态
     * @param userId      用户id
     * @return 没有作废的实例
     */
    List<WorkflowInstanceEntity> findAllByIdInAndStatusNotAndCreatedBy(List<String> instanceIds, Integer status, Long userId);

    /**
     * 更改流程状态为作废状态
     *
     * @param businessKey 业务主键
     * @param status      作废状态码
     * @return 影响行数
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update WorkflowInstanceEntity set status=:status where businessKey in (:businessKey)")
    int updateInvalidStatus(@Param("businessKey") List<String> businessKey, @Param("status") Integer status);

    /**
     * 更改流程状态为作废状态
     *
     * @param instanceIds 流程ids
     * @param status      流程状态
     * @return 影响行数
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update WorkflowInstanceEntity set status=:status where id in (:instanceIds)")
    int updateInstanceStatusById(@Param("instanceIds") List<String> instanceIds, @Param("status") Integer status);

    /**
     * 通过任务查询实例
     *
     * @param taskId 任务ID
     * @return 实例
     */
    @Query("select i from WorkflowInstanceEntity i left join TaskEntity t on i.id = t.instanceId where t.id=:taskId")
    WorkflowInstanceEntity getInstanceByTaskId(@Param("taskId") String taskId);

    /**
     * 通过流程定义key获取
     *
     * @param definitionKey  流程定义key
     * @param taskStatusList 任务状态列表
     * @return 流程实例列表
     */
    @Query("select distinct i " +
            " from WorkflowInstanceEntity i " +
            " left join WorkflowDefinitionEntity d on i.definitionId = d.id " +
            " left join TaskEntity t on i.id = t.instanceId " +
            " where d.initialCode = :definitionKey and t.status in (:taskStatusList) and i.status=1")
    List<WorkflowInstanceEntity> getIncompleteInstanceByDefinitionKeyAndTaskStatusIn(@Param("definitionKey") String definitionKey,
                                                                                     @Param("taskStatusList") List<Integer> taskStatusList);

    /**
     * 获取未完成实例任务信息
     *
     * @param taskStatusList 任务状态列表
     * @param definitionKey  流程定义key
     * @return 未完成实例及任务详情
     */
    @Query("select new com.ruigu.rbox.workflow.model.entity.WorkflowInstanceWithTaskEntity(i.id,i.name,i.businessKey,i.businessUrl,i.pcBusinessUrl,i.mobileBusinessUrl," +
            " i.definitionId,i.definitionCode,i.definitionVersion,i.startTime,i.endTime,i.deleteReason,i.ownerId, " +
            " i.sourcePlatform,i.sourcePlatformUserId,i.sourcePlatformUserName,i.createdBy,i.createdOn,i.lastUpdatedBy,i.lastUpdatedOn,i.status, " +
            " t.id,t.nodeId,t.modelId,t.graphId,t.name,t.description,t.type,t.approvalNode,t.dueTime,t.candidateUsers,t.candidateGroups, " +
            " t.submitBy,t.submitTime,t.beginTime,t.createdBy,t.createdOn,t.lastUpdatedBy,t.lastUpdatedOn,t.status,t.remarks) " +
            " from WorkflowInstanceEntity i " +
            " left join WorkflowDefinitionEntity d on i.definitionId = d.id " +
            " left join TaskEntity t on i.id = t.instanceId " +
            " where d.initialCode = :definitionKey and t.status in (:taskStatusList) and i.status=1")
    List<WorkflowInstanceWithTaskEntity> getIncompleteInstanceWithTaskByDefinitionKeyAndTaskStatusIn(@Param("definitionKey") String definitionKey,
                                                                                                     @Param("taskStatusList") List<Integer> taskStatusList);

    /**
     * 批量查询通过id
     *
     * @param ids       id
     * @param createdBy 创建人id
     * @return 实例
     */
    List<WorkflowInstanceEntity> findAllByIdInAndCreatedBy(List<String> ids, Long createdBy);

    /**
     * 通过流程定义和业务主键搜索实例
     *
     * @param definitionKey 流程定义key
     * @param businessKey   业务参数
     * @return 流程实例
     */
    WorkflowInstanceEntity findByDefinitionCodeAndBusinessKey(String definitionKey, String businessKey);

    /**
     * 通过流程实例，业务主键，和状态查询
     *
     * @param definitionCode  流程key
     * @param businessKeyList 业务主键列表
     * @param status          状态
     * @return 流程列表
     */
    List<WorkflowInstanceEntity> findAllByDefinitionCodeAndBusinessKeyInAndStatusNot(String definitionCode, List<String> businessKeyList, Integer status);
}
