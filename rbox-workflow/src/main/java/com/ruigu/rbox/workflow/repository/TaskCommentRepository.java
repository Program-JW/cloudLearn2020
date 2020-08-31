package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.TaskCommentEntity;
import com.ruigu.rbox.workflow.model.vo.TaskCommentVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liqingtian
 * @date 2019/08/26 19:17
 */
@Service
public interface TaskCommentRepository extends JpaRepository<TaskCommentEntity, Integer> {

    /**
     * 查询该任务下所有评论列表
     *
     * @param taskId 任务id
     * @return 该任务下所有评论列表
     */
    @Query(value = "select new com.ruigu.rbox.workflow.model.vo.TaskCommentVO(t.id,t.content,t.parentId,t.createdOn,t.createdBy,t.status,t.taskId) " +
            " from TaskCommentEntity t where t.taskId =:taskId and t.status = 1 order by t.createdOn desc ")
    List<TaskCommentVO> selectCommentByTaskId(@Param("taskId") String taskId);

    /**
     * 查询二级评论
     *
     * @param commentId 一级评论id
     * @return 二级评论列表
     */
    @Query(value = "select new com.ruigu.rbox.workflow.model.vo.TaskCommentVO(t.id,t.content,t.parentId,t.createdOn,t.createdBy,t.status,t.taskId) " +
            " from TaskCommentEntity t where t.parentId = :commentId and t.status = 1 order by t.createdOn desc ")
    List<TaskCommentVO> selectCommentByCommentId(@Param("commentId") Integer commentId);

    /**
     * 更改评论状态
     *
     * @param commentId 评论id
     */
    @Modifying
    @Query("update TaskCommentEntity t set t.status = 0 where t.id = :commentId ")
    void updateStatus(Integer commentId);

    /**
     * 通过状态和id查找评论
     *
     * @param commentId 评论id
     * @param status    状态
     * @return 评论信息实体
     */
    TaskCommentEntity findByIdAndStatus(Integer commentId, Integer status);

    /**
     * 获取实例的所有评论
     *
     * @param instanceId 实例ID
     * @return List<TaskCommentVO>
     */
    @Query(" select new com.ruigu.rbox.workflow.model.vo.TaskCommentVO(c.id,c.content,c.parentId,c.createdOn,c.createdBy,c.status,c.taskId,t.name) " +
            " from WorkflowInstanceEntity i left join TaskEntity t on i.id = t.instanceId " +
            " left join TaskCommentEntity c on t.id = c.taskId " +
            " where i.id = :instanceId " +
            " and c.status = 1 " +
            " order by c.createdOn desc ")
    List<TaskCommentVO> selectCommentByInstanceId(@Param("instanceId") String instanceId);
}
