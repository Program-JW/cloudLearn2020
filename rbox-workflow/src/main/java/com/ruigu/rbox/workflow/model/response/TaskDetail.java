package com.ruigu.rbox.workflow.model.response;

import com.ruigu.rbox.workflow.model.entity.TaskEntity;
import com.ruigu.rbox.workflow.model.vo.TaskCommentVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author alan.zhao
 */
@Data
@ApiModel(value = "任务详情")
public class TaskDetail extends TaskEntity {
    @ApiModelProperty(value = "业务数据的唯一标识符",name = "orderNumber")
    private String orderNumber;
    private String detailUrl;
    @ApiModelProperty(value = "流程实例id",name = "processId")
    private String processId;
    @ApiModelProperty(value = "流程实例的名称",name = "processName")
    private String processName;
    @ApiModelProperty(value = "流程实例创建人名称",name = "processCreatedBy")
    private String processCreatedBy;
    @ApiModelProperty(value = "任务提交人名称",name = "submitName")
    private String submitName;
    @ApiModelProperty(value = "业务详情页面",name = "businessUrl")
    private String businessUrl;
    @ApiModelProperty(value = "业务详情页面(PC版)",name = "pcBusinessUrl")
    private String pcBusinessUrl;
    @ApiModelProperty(value = "业务详情页面(移动端版)",name = "mobileBusinessUrl")
    private String mobileBusinessUrl;
    @ApiModelProperty(value = "流程实例的创建时间",name = "processCreatedOn")
    private Date processCreatedOn;
    @ApiModelProperty(value = "表单",name = "form")
    private Form form;
    @ApiModelProperty(value = "流程初始表单及数据",name = "initialForm")
    private Form initialForm;
    @ApiModelProperty(value = "任务评论列表",name = "comments")
    private List<TaskCommentVO> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        TaskDetail that = (TaskDetail) o;
        return Objects.equals(orderNumber, that.orderNumber) &&
                Objects.equals(detailUrl, that.detailUrl) &&
                Objects.equals(processId, that.processId) &&
                Objects.equals(processName, that.processName) &&
                Objects.equals(processCreatedBy, that.processCreatedBy) &&
                Objects.equals(submitName, that.submitName) &&
                Objects.equals(businessUrl, that.businessUrl) &&
                Objects.equals(pcBusinessUrl, that.pcBusinessUrl) &&
                Objects.equals(mobileBusinessUrl, that.mobileBusinessUrl) &&
                Objects.equals(processCreatedOn, that.processCreatedOn) &&
                Objects.equals(form, that.form) &&
                Objects.equals(initialForm, that.initialForm) &&
                Objects.equals(comments, that.comments);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), orderNumber, detailUrl, processId, processName, processCreatedBy, submitName, businessUrl, pcBusinessUrl, mobileBusinessUrl, processCreatedOn, form, initialForm, comments);
    }
}
