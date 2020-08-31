package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.entity.TaskCommentEntity;
import com.ruigu.rbox.workflow.model.vo.TaskCommentVO;
import com.ruigu.rbox.workflow.service.TaskCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author liqingtian
 * @date 2019/08/26 20:19
 */
@Controller
public class TaskCommentController {

    @Autowired
    private TaskCommentService taskCommentService;

    @GetMapping("/task/comment")
    @ResponseBody
    public ServerResponse getComments(String taskId) throws Exception {
        List<TaskCommentVO> comments = taskCommentService.getCommentByTaskId(taskId);
        return ServerResponse.ok(comments);
    }

    @GetMapping("/task/comment/second/{commentId}")
    @ResponseBody
    public ServerResponse getSecondComments(@PathVariable Integer commentId) {
        List<TaskCommentVO> comments = taskCommentService.getCommentByParendId(commentId);
        return ServerResponse.ok(comments);
    }

    @PostMapping("/task/comment/add")
    @ResponseBody
    public ServerResponse saveComment(@RequestBody @Valid TaskCommentEntity taskCommentEntity) throws Exception {
        taskCommentService.saveComment(taskCommentEntity);
        return ServerResponse.ok();
    }

    @PostMapping("/task/comment/delete/{commentId}")
    @ResponseBody
    public ServerResponse removeComment(@PathVariable Integer commentId) throws Exception {
        taskCommentService.removeCommentById(commentId);
        return ServerResponse.ok();
    }
}
