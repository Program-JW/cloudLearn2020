package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.feign.PassportFeignClient;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.entity.TaskCommentEntity;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.vo.TaskCommentVO;
import com.ruigu.rbox.workflow.repository.TaskCommentRepository;
import com.ruigu.rbox.workflow.service.TaskCommentService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/08/26 19:16
 */
@Service
public class TaskCommentServiceImpl implements TaskCommentService {

    @Resource
    private TaskCommentRepository taskCommentRepository;

    @Resource
    private PassportFeignClient passPortFeignClient;

    @Override
    public List<TaskCommentVO> getCommentByTaskId(String taskId) {

        if (StringUtils.isBlank(taskId)) {
            throw new VerificationFailedException(400, "所查询任务Id为空");
        }

        List<TaskCommentVO> comments = taskCommentRepository.selectCommentByTaskId(taskId);
        if (CollectionUtils.isEmpty(comments)) {
            return null;
        }

        // 设置作者
        comments = setAuthor(comments);

        // 组装一二级评论
        Map<Integer, TaskCommentVO> commentMap = new HashMap<>(16);
        Map<Integer, List<TaskCommentVO>> childrenMap = new HashMap<>(16);
        comments.forEach(comment -> {
            if (comment.getParentId() == 0) {
                commentMap.put(comment.getId(), comment);
            } else {
                if (!childrenMap.containsKey(comment.getParentId())) {
                    childrenMap.put(comment.getParentId(), new ArrayList<>());
                }
                childrenMap.get(comment.getParentId()).add(comment);
            }
        });
        List<TaskCommentVO> commentList = new ArrayList<>();
        commentMap.forEach((key, value) -> {
            if (childrenMap.containsKey(key)) {
                commentMap.get(key).setChildren(childrenMap.get(key));
            }
            commentList.add(commentMap.get(key));
        });

        return commentList;
    }

    @Override
    public void saveComment(TaskCommentEntity taskComment) {
        // 当前登陆人
        Integer userId = UserHelper.getUserId();
        taskComment.setCreatedBy(userId);
        taskComment.setLastUpdatedBy(userId);
        taskCommentRepository.save(taskComment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeCommentById(Integer commentId) {
        TaskCommentEntity comment = taskCommentRepository.findByIdAndStatus(commentId, 1);
        if (comment == null) {
            throw new VerificationFailedException(ResponseCode.INTERNAL_ERROR.getCode(), "您所删除的评论不存在，无法删除。");
        }
        comment.setStatus(0);
        taskCommentRepository.save(comment);
    }

    @Override
    public List<TaskCommentVO> getCommentByParendId(Integer commentId) {
        TaskCommentEntity comment = taskCommentRepository.findByIdAndStatus(commentId, 1);
        if (comment == null) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "该评论已不存在");
        }
        List<TaskCommentVO> comments = taskCommentRepository.selectCommentByCommentId(commentId);
        if (CollectionUtils.isEmpty(comments)) {
            return null;
        }
        return setAuthor(comments);
    }

    @Override
    public List<TaskCommentVO> getCommentByInstanceId(String instanceId) {
        if (StringUtils.isBlank(instanceId)) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "流程实例ID不能为空");
        }
        List<TaskCommentVO> comments = taskCommentRepository.selectCommentByInstanceId(instanceId);
        if (CollectionUtils.isEmpty(comments)) {
            return null;
        }
        return setAuthor(comments);
    }

    /**
     * 设置评论人名字
     *
     * @param comments 评论列表
     * @return List<TaskCommentVO>
     */
    private List<TaskCommentVO> setAuthor(List<TaskCommentVO> comments) {
        List<Integer> authorIds = new ArrayList<>();
        comments.forEach(comment -> authorIds.add(comment.getCreatedBy()));
        ServerResponse<List<PassportUserInfoDTO>> userResponse = passPortFeignClient.getUserMsgByIds(authorIds);
        if (userResponse.getCode() == ResponseCode.SUCCESS.getCode()) {
            List<PassportUserInfoDTO> userList = userResponse.getData();
            if (CollectionUtils.isNotEmpty(userList)) {
                comments.forEach(comment -> userList.stream()
                        .filter(user -> user.getId().equals(comment.getCreatedBy()))
                        .findFirst().ifPresent(info -> comment.setAuthor(info.getNickname())));
            }
        }
        return comments;
    }
}
