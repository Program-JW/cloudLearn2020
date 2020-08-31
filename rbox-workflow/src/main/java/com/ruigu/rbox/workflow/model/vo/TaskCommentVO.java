package com.ruigu.rbox.workflow.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liqingtian
 * @date 2019/08/26 19:05
 */
@Data
public class TaskCommentVO {

    private Integer id;
    private String content;
    private Integer parentId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdOn;
    private Integer createdBy;
    private String author;
    private Integer status;
    private String taskId;
    private String taskName;
    private List<TaskCommentVO> children;

    public TaskCommentVO(Integer id, String content, Integer parentId, Date createdOn, Integer createdBy, Integer status,String taskId) {
        this.id = id;
        this.content = content;
        this.parentId = parentId;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.status = status;
        this.taskId = taskId;
    }

    public TaskCommentVO(Integer id, String content, Integer parentId, Date createdOn, Integer createdBy, Integer status, String taskId, String taskName) {
        this.id = id;
        this.content = content;
        this.parentId = parentId;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.status = status;
        this.taskId = taskId;
        this.taskName = taskName;
    }
}
