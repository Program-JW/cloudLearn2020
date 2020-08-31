package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.WorkflowHistoryEntity;
import com.ruigu.rbox.workflow.model.entity.WorkflowInstanceEntity;
import com.ruigu.rbox.workflow.model.request.SearchInstanceRequest;
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
public interface WorkflowHistoryRepository extends JpaRepository<WorkflowHistoryEntity, String>, JpaSpecificationExecutor<WorkflowHistoryEntity> {

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
            " from WorkflowHistoryEntity i left join WorkflowDefinitionEntity d " +
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
     * @param req      查找参数
     * @param pageable 分页参数
     * @param ownerId  负责人id
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
            " from WorkflowHistoryEntity i left join WorkflowDefinitionEntity d " +
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
     * @param request  查找参数
     * @param pageable 分页参数
     * @param createId 负责人id
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
            " from WorkflowHistoryEntity i left join WorkflowDefinitionEntity d " +
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
            " from WorkflowHistoryEntity i left join WorkflowDefinitionEntity d " +
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
            " from WorkflowDefinitionEntity d left join WorkflowHistoryEntity i " +
            " on d.id = i.definitionId " +
            " left join TaskEntity t " +
            " on i.id = t.instanceId " +
            " where t.id = :id ")
    WorkflowInstanceVO selectInstanceVOByTaskId(@Param("id") String id);

    /**
     * 通过businessKey 和 流程状态 查询实例
     *
     * @param businessKeys 业务流程key
     * @param status       状态
     * @return 所有没有作废的实例
     */
    List<WorkflowHistoryEntity> findAllByBusinessKeyInAndStatusNot(List<String> businessKeys, Integer status);

    /**
     * 通过businessKey 和 流程状态 查询实例
     *
     * @param definitionKey 流程key
     * @param businessKeys  业务流程key
     * @param status        状态
     * @return 所有没有作废的实例
     */
    List<WorkflowHistoryEntity> findAllByDefinitionCodeAndBusinessKeyInAndStatusNot(String definitionKey, List<String> businessKeys, Integer status);

    /**
     * 通过businessKey 和 流程状态  和 创建人 查询实例
     *
     * @param businessKeys 业务流程key
     * @param status       状态
     * @param userId       用户
     * @return 所有没有作废的实例
     */
    List<WorkflowHistoryEntity> findAllByBusinessKeyInAndStatusNotAndCreatedBy(List<String> businessKeys, Integer status, Long userId);

    /**
     * 通过流程ID 和 流程状态 和 创建人id 查询实例
     *
     * @param instanceId 流程ID
     * @param status     状态
     * @param userId     用户id
     * @return 没有作废的实例
     */
    List<WorkflowHistoryEntity> findAllByIdAndStatusNotAndCreatedBy(String instanceId, Integer status, Long userId);

    /**
     * 更改流程状态为作废状态
     *
     * @param businessKey 业务主键
     * @param status      作废状态码
     * @return 影响行数
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update WorkflowHistoryEntity set status=:status where businessKey in (:businessKey)")
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
    @Query("update WorkflowHistoryEntity set status=:status where id in (:instanceIds)")
    int updateInstanceStatusById(@Param("instanceIds") List<String> instanceIds, @Param("status") Integer status);

    /***
     * 批量查询通过id
     * @param ids id列表
     * @return 实例
     */
    List<WorkflowHistoryEntity> findAllByIdIn(List<String> ids);

    /**
     * 通过流程定义和业务主键搜索实例
     *
     * @param definitionKey 流程定义key
     * @param businessKey   业务参数
     * @return 流程实例
     */
    WorkflowHistoryEntity findByDefinitionCodeAndBusinessKey(String definitionKey, String businessKey);

    /**
     * 查询非作废状态下的流程
     *
     * @param definitionKey 流程定义key
     * @param businessKey   业务参数
     * @param status        状态
     * @return 流程实例
     */
    WorkflowHistoryEntity findByDefinitionCodeAndBusinessKeyAndStatusNot(String definitionKey, String businessKey, Integer status);
}
