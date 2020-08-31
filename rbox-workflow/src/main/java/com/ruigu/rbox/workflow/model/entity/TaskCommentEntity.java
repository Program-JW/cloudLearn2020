package com.ruigu.rbox.workflow.model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author liqingtian
 * @date 2019/08/26 19:25
 */
@Entity
@Table(name = "task_comment")
public class TaskCommentEntity {
    private Integer id;
    private String content;
    private Integer parentId;
    private Integer createdBy;
    private Timestamp createdOn;
    private Integer lastUpdatedBy;
    private Timestamp lastUpdatedOn;
    private Integer status;
    private String taskId;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "content")
    @NotBlank(message = "评论内容不能为空")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "parent_id")
    @NotNull(message = "评论父id,不能为空")
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "created_by")
    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    @Basic
    @Column(name = "created_on", insertable = false,updatable = false)
    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    @Basic
    @Column(name = "last_updated_by")
    public Integer getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Integer lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Basic
    @Column(name = "last_updated_on", insertable = false,updatable = false)
    public Timestamp getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Timestamp lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    @Basic
    @Column(name = "status", insertable = false)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "task_id")
    @NotBlank(message = "任务id不能为空")
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskCommentEntity that = (TaskCommentEntity) o;
        return id.equals(that.id) &&
                Objects.equals(content, that.content) &&
                Objects.equals(parentId, that.parentId) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdOn, that.createdOn) &&
                Objects.equals(lastUpdatedBy, that.lastUpdatedBy) &&
                Objects.equals(lastUpdatedOn, that.lastUpdatedOn) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, parentId, createdBy, createdOn, lastUpdatedBy, lastUpdatedOn, status);
    }
}
