package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.NodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author alan.zhao
 */
public interface NodeRepository extends JpaRepository<NodeEntity, String>, JpaSpecificationExecutor<NodeEntity> {

    /**
     * 查询流程定义的所有节点
     * @param modelId 流程定义ID
     * @return 查询流程定义的所有节点
     */
    List<NodeEntity> findAllByModelId(String modelId);

    /**
     * 批量删除
     * @param idList 节点id列表
     */
    void deleteAllByIdIn(List<String> idList);

    /**
     * 批量删除指定流程定义的节点信息
     * @param modelId 流程定义ID
     */
    void deleteAllByModelId(String modelId);

    /**
     * 根据部署信息结合节点标识符查询节点
     * @param deploymentId 流程部署实体的id
     * @param graphId xml中的id
     * @return 查询流程定义的所有节点
     */
    @Query("select n from NodeEntity n,WorkflowDefinitionEntity def where n.modelId=def.id and def.deploymentId=?1 and n.graphId=?2")
    NodeEntity fromDeploymentInfo(String deploymentId, String graphId);
}
