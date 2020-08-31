package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.WorkflowDefinitionEntity;
import com.ruigu.rbox.workflow.model.entity.WorkflowInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author alan.zhao
 */
public interface WorkflowDefinitionRepository extends JpaRepository<WorkflowDefinitionEntity, String>, JpaSpecificationExecutor<WorkflowDefinitionEntity> {

    /**
     * 查询流程的最大版本号
     *
     * @param id 流程ID
     * @return 版本号
     */
    @Query("select max(e.version)  from WorkflowDefinitionEntity e where e.isReleased=1 and e.initialCode=(select e1.initialCode from WorkflowDefinitionEntity e1 where e1.id=?1)")
    Integer maxVersion(String id);

    /**
     * 查询流程的最新发布版
     *
     * @param initialCode 流程key
     * @return 最新版
     */
    @Query("select def from WorkflowDefinitionEntity def where def.initialCode=?1 and  def.version=(select max(e.version)  from WorkflowDefinitionEntity e where e.isReleased=1 and e.initialCode=?1)")
    WorkflowDefinitionEntity latestReleased(String initialCode);

    /**
     * 查询流程的最新草稿
     *
     * @param initialCode 流程key
     * @return 最新草稿
     */
    @Query("select def from WorkflowDefinitionEntity def where def.initialCode=?1 and  def.version=(select max(e.version)  from WorkflowDefinitionEntity e where e.isReleased=0 and e.initialCode=?1)")
    WorkflowDefinitionEntity latestDraft(String initialCode);

    /**
     * 查询流程历史版本
     *
     * @param initialCode 流程key
     * @return 流程历史版本
     */
    @Query("select def from WorkflowDefinitionEntity def where def.initialCode=?1 and def.status>-1 order by def.version desc ")
    List<WorkflowDefinitionEntity> versions(String initialCode);

    /**
     * 是否是草稿
     *
     * @param id 流程ID
     * @return 是否是草稿
     */
    @Query("select count(def) from WorkflowDefinitionEntity def where def.id=?1 and def.isReleased=0")
    long isDraft(String id);

    /**
     * 只查询名字和id
     *
     * @return List<WorkflowDefinitionEntity> 定义列表
     */
    @Query("select new com.ruigu.rbox.workflow.model.entity.WorkflowDefinitionEntity(d.id,d.name,d.version) from WorkflowDefinitionEntity d where d.isReleased=1 order by d.name,d.version desc ")
    List<WorkflowDefinitionEntity> selectAllNameAndIdAndVersion();

    /**
     * 查询code对应的最新版本的定义
     *
     * @param definitionKey 流程定义initialCode
     * @return 返回流程定义实体
     * @author ：jianghuilin
     * @date ：Created in {2019/9/5} {9:58}
     */
    @Query("select def " +
            " from WorkflowDefinitionEntity def " +
            " where def.version=(select MAX(version) from WorkflowDefinitionEntity where initialCode=?1) " +
            " and def.initialCode=?1 ")
    WorkflowDefinitionEntity findNewDefinition(String definitionKey);

    /**
     * 查询code是否被使用
     *
     * @param initialCode 流程initialCode
     * @return true存在, 否则不存在
     * @author ：alan.zhao
     * @date ：2019-09-18 11:22:24
     */
    boolean existsByInitialCode(String initialCode);

    /**
     * 通过流程id获取该任务流程定义
     *
     * @param instanceId 流程id
     * @return 流程定义实例
     */
    @Query(" select d " +
            " from WorkflowDefinitionEntity d " +
            " left join WorkflowInstanceEntity i on d.id = i.definitionId " +
            " where i.id = :instanceId ")
    WorkflowDefinitionEntity findByInstanceId(@Param("instanceId") String instanceId);

    /**
     * 通过流程id获取该任务流程定义
     *
     * @param instanceIds 流程id
     * @return 流程定义实例
     */
    @Query(" select d " +
            " from WorkflowDefinitionEntity d " +
            " left join WorkflowInstanceEntity i on d.id = i.definitionId " +
            " where i.id in (:instanceIds) ")
    List<WorkflowDefinitionEntity> findByInstanceIdIn(@Param("instanceIds") List<String> instanceIds);

    /**
     * 通过任务id获取该任务流程定义
     *
     * @param taskId 任务id
     * @return 流程定义实例
     */
    @Query(" select d " +
            " from WorkflowDefinitionEntity d " +
            " left join WorkflowInstanceEntity i on d.id = i.definitionId " +
            " left join TaskEntity t on i.id = t.instanceId " +
            " where t.id=:taskId ")
    WorkflowDefinitionEntity findByTaskId(String taskId);

    /**
     * 获取发布的启用的所有流程定义通过流程code
     *
     * @param definitionCode 流程初始定义code
     * @param isReleased     是否发布
     * @param status         是否启用
     * @return 符合条件的所有版本的流程
     */
    List<WorkflowDefinitionEntity> findAllByInitialCodeAndIsReleasedAndStatus(String definitionCode, Integer isReleased, Integer status);

    /**
     * 批量获取流程定义信息
     *
     * @param ids 流程定义ids
     * @return 流程实例列表
     */
    List<WorkflowDefinitionEntity> findAllByIdIn(List<String> ids);
}
