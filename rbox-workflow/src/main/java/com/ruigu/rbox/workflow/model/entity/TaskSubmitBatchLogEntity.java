package com.ruigu.rbox.workflow.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author liqingtian
 * @date 2019/10/08 16:16
 */
@ApiModel(value = "查询批处理提交进度信息实体")
@Entity
@Table(name = "task_submit_batch_log")
public class TaskSubmitBatchLogEntity {
    @ApiModelProperty(value = "批处理id",name = "id")
    private Integer id;
    @ApiModelProperty(value = "总数",name = "total")
    private Integer total;
    @ApiModelProperty(value = "失败条数",name = "fail")
    private Integer fail;
    @ApiModelProperty(value = "成功条数",name = "success")
    private Integer success;
    @ApiModelProperty(value = "失败的任务id",name = "failId")
    private String failId;
    @ApiModelProperty(value = "成功的任务id",name = "successId")
    private String successId;
    @ApiModelProperty(value = "创建时间",name = "createdOn")
    private Timestamp createdOn;
    @ApiModelProperty(value = "创建人id",name = "createdBy")
    private Integer createdBy;

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
    @Column(name = "total")
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Basic
    @Column(name = "fail")
    public Integer getFail() {
        return fail;
    }

    public void setFail(Integer fail) {
        this.fail = fail;
    }

    @Basic
    @Column(name = "success")
    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    @Basic
    @Column(name = "fail_id")
    public String getFailId() {
        return failId;
    }

    public void setFailId(String failId) {
        this.failId = failId;
    }

    @Basic
    @Column(name = "success_id")
    public String getSuccessId() {
        return successId;
    }

    public void setSuccessId(String successId) {
        this.successId = successId;
    }

    @Basic
    @Column(name = "created_on", insertable = false)
    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    @Basic
    @Column(name = "created_by")
    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskSubmitBatchLogEntity that = (TaskSubmitBatchLogEntity) o;
        return id.equals(that.id) &&
                Objects.equals(total, that.total) &&
                Objects.equals(fail, that.fail) &&
                Objects.equals(success, that.success) &&
                Objects.equals(failId, that.failId) &&
                Objects.equals(successId, that.successId) &&
                Objects.equals(createdOn, that.createdOn) &&
                Objects.equals(createdBy, that.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, total, fail, success, failId, successId, createdOn, createdBy);
    }
}
