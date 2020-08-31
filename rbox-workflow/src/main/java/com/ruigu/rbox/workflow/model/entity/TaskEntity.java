package com.ruigu.rbox.workflow.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author alan.zhao
 */
@Entity
@Table(name = "task")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class TaskEntity implements Serializable {
    @Id
    @ApiModelProperty(value = "任务id", name = "id")
    private String id;
    @ApiModelProperty(value = "所属任务定义的ID", name = "nodeId")
    private String nodeId;
    @ApiModelProperty(value = "所属流程定义的ID", name = "modelId")
    private String modelId;
    @ApiModelProperty(value = "流程实例ID", name = "instanceId")
    private String instanceId;
    @ApiModelProperty(value = "流程图中的节点id", name = "graphId")
    private String graphId;
    @ApiModelProperty(value = "任务名称", name = "name")
    private String name;
    @ApiModelProperty(value = "任务描述", name = "description")
    private String description;
    @ApiModelProperty(value = "节点类型,1 用户任务", name = "type")
    private Integer type;
    @ApiModelProperty(value = "是否是审批节点 1 是 0 否", name = "approvalNode")
    private Integer approvalNode;
    @ApiModelProperty(value = "工作时间数,单位是分钟", name = "dueTime")
    private String dueTime;
    @ApiModelProperty(value = "候选用户,逗号分隔的id列表", name = "candidateUsers")
    private String candidateUsers;
    @ApiModelProperty(value = "候选用户组,逗号分隔的id列表", name = "candidateGroups")
    private String candidateGroups;
    @ApiModelProperty(value = "表单位置", name = "formLocation")
    private String formLocation;
    @Lob
    @ApiModelProperty(value = "表单内容", name = "formContent")
    private String formContent;
    @Lob
    @ApiModelProperty(value = "任务数据", name = "data")
    private String data;
    @ApiModelProperty(value = "提交人ID", name = "submitBy")
    private Long submitBy;
    @ApiModelProperty(value = "提交时间", name = "submitTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submitTime;
    @ApiModelProperty(value = "任务开始时间", name = "beginTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date beginTime;
    @ApiModelProperty(value = "创建者id", name = "createdBy")
    private Long createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "创建时间", name = "createdOn")
    private Date createdOn;
    @ApiModelProperty(value = "最后修改者id", name = "lastUpdatedBy")
    private Long lastUpdatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "最后修改日期", name = "lastUpdatedOn")
    private Date lastUpdatedOn;
    @ApiModelProperty(value = "状态：0未处理，1 处理中, 2 处理完成", name = "status")
    private Integer status;
    @ApiModelProperty(value = "通知类型", name = "noticeType")
    private Integer noticeType;
    @ApiModelProperty(value = "通知内容", name = "noticeContent")
    private String noticeContent;
    @ApiModelProperty(value = "任务详情的请求链接（现在没用了）", name = "businessUrl")
    private String businessUrl;
    @ApiModelProperty(value = "备注", name = "remarks")
    private String remarks;
    @ApiModelProperty(value = "概要信息模板", name = "summary")
    @Lob
    private String summary;
    @ApiModelProperty(value = "版本号", name = "version")
    @Version
    private Integer version;

    public TaskEntity() {
    }

    public TaskEntity(String id, String name, String instanceId, Integer status, Long submitBy, Date submitTime, Integer noticeType, String noticeContent) {
        this.id = id;
        this.name = name;
        this.instanceId = instanceId;
        this.status = status;
        this.submitBy = submitBy;
        this.submitTime = submitTime;
        this.noticeType = noticeType;
        this.noticeContent = noticeContent;
    }

    public TaskEntity(String id, String instanceId, String data) {
        this.id = id;
        this.instanceId = instanceId;
        this.data = data;
    }
}
