package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.entity.TaskCommentEntity;
import com.ruigu.rbox.workflow.model.vo.TaskCommentVO;

import java.util.List;

/**
 * @author liqingtian
 * @date 2019/08/26 19:11
 */
public interface TaskCommentService {

    /**
     * 获取一级评论
     *
     * @param taskId 任务id
     * @return 一级评论列表
     * @throws Exception e
     */
    List<TaskCommentVO> getCommentByTaskId(String taskId) throws Exception;

    /**
     * 保存
     *
     * @param taskComment 评论信息实体
     */
    void saveComment(TaskCommentEntity taskComment);

    /**
     * 删除
     *
     * @param commentId 评论id
     * @throws Exception e
     */
    void removeCommentById(Integer commentId) throws Exception;

    /**
     * 获取二级评论
     *
     * @param commentId 评论id
     * @return 二级评论列表
     */
    List<TaskCommentVO> getCommentByParendId(Integer commentId);

    /**
     * 获取实例下所有评论
     *
     * @param instanceId 实例id
     * @return 所有评论
     * @throws Exception e
     */
    List<TaskCommentVO> getCommentByInstanceId(String instanceId) throws Exception;
}
