package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.WorkflowFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author ：jianghuilin
 * @date ：Created in {2019/8/29} {10:15}
 */
public interface WorkflowFormRepository extends JpaRepository<WorkflowFormEntity, String>, JpaSpecificationExecutor<WorkflowFormEntity> {
    /**
     * 通过定义流程来查找code
     *
     * @param definitionId 流程定义id
     * @return 流程启动表单实体类
     */
    WorkflowFormEntity findTopBydefinitionId(String definitionId);

    /**
     * 通过定义流程ID来查找
     *
     * @param definitionId 流程定义id
     * @return 流程启动表单实体类
     */
    WorkflowFormEntity findByDefinitionId(String definitionId);

    /**
     * 通过任务id查找
     *
     * @param taskId 任务id
     * @return WorkflowFormEntity
     */
    @Query("select f from WorkflowFormEntity f " +
            " inner join WorkflowInstanceEntity i on f.definitionId = i.definitionId " +
            " inner join TaskEntity t on t.instanceId = i.id " +
            " where t.id = :taskId ")
    WorkflowFormEntity selectByTaskId(@Param("taskId") String taskId);

    /**
     * 通过实例id获取初始表单
     *
     * @param instanceId 流程实例id
     * @return 流程启动表单实体类
     */
    @Query("select f from WorkflowFormEntity f " +
            " inner join WorkflowInstanceEntity i on f.definitionId = i.definitionId " +
            " where i.id = :instanceId ")
    WorkflowFormEntity selectByInstanceId(@Param("instanceId") String instanceId);

    /**
     * 通过定义流程ID来删除表单
     *
     * @param definitionId 流程定义id
     */
    void deleteAllByDefinitionId(String definitionId);
}
