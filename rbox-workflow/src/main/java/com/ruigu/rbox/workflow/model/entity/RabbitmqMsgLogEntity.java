package com.ruigu.rbox.workflow.model.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * @author liqingtian
 * @date 2019/09/10 18:17
 */
@Entity
@Table(name = "rabbitmq_msg")
public class RabbitmqMsgLogEntity {
    private int id;
    private String message;
    private Date createdOn;
    private Integer status;
    private String taskIdWeixin;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Basic
    @Column(name = "created_on", updatable = false)
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
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
    @Column(name = "task_id_weixin")
    public String getTaskIdWeixin() {
        return taskIdWeixin;
    }

    public void setTaskIdWeixin(String taskIdWeixin) {
        this.taskIdWeixin = taskIdWeixin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RabbitmqMsgLogEntity that = (RabbitmqMsgLogEntity) o;
        return id == that.id &&
                Objects.equals(message, that.message) &&
                Objects.equals(createdOn, that.createdOn) &&
                Objects.equals(status, that.status) &&
                Objects.equals(taskIdWeixin, that.taskIdWeixin);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, message, createdOn, status, taskIdWeixin);
    }
}
