package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.WorkflowCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 流程分类数据库操作
 *
 * @author ：jianghuilin
 * @date ：Created in {2019/8/28} {16:14}
 */
public interface WorkflowCategoryRepository extends JpaRepository<WorkflowCategoryEntity, String>, JpaSpecificationExecutor<WorkflowCategoryEntity> {
    /**
     * 查询没父节点的流程分类
     *
     * @param parentId 父分类ID
     * @return 分类列表
     */
    List<WorkflowCategoryEntity> findByParentId(Integer parentId);

    /**
     * 通过Id查找流程分类对象
     *
     * @param id 分类ID
     * @return 分类实体
     */
    WorkflowCategoryEntity findTopById(Integer id);
}
