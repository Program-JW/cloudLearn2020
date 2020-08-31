package com.ruigu.rbox.workflow.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author liqingtian
 * @date 2019/09/02 16:32
 */
@Data
public class WorkflowInstanceVO {

    private String id;
    private String name;
    private String businessKey;
    private String businessUrl;
    private String pcBusinessUrl;
    private String mobileBusinessUrl;
    private String definitionId;
    private String definitionCode;
    /**
     * 扩展字段 流程定义名
     */
    private String definitionName;
    private Integer definitionVersion;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    private String deleteReason;
    private Long ownerId;
    /**
     * 扩展字段 负责人姓名
     */
    private String ownerName;
    private Long createdBy;
    /**
     * 扩展字段 创建人人姓名
     */
    private String creator;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdOn;
    private Long lastUpdatedBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdatedOn;
    private Integer status;

    /**
     * 扩展字段 判断是否为历史流程（已完成流程）
     */
    private Boolean history;

    public WorkflowInstanceVO() {
    }

    public WorkflowInstanceVO(String id, String name, String businessKey, String businessUrl, String pcBusinessUrl, String mobileBusinessUrl, String definitionId, String definitionCode, String definitionName, Integer definitionVersion, Date startTime, Date endTime, String deleteReason, Long ownerId, Long createdBy, Date createdOn, Long lastUpdatedBy, Date lastUpdatedOn, Integer status) {
        this.id = id;
        this.name = name;
        this.businessKey = businessKey;
        this.businessUrl = businessUrl;
        this.pcBusinessUrl = pcBusinessUrl;
        this.mobileBusinessUrl = mobileBusinessUrl;
        this.definitionId = definitionId;
        this.definitionCode = definitionCode;
        this.definitionName = definitionName;
        this.definitionVersion = definitionVersion;
        this.startTime = startTime;
        this.endTime = endTime;
        this.deleteReason = deleteReason;
        this.ownerId = ownerId;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdatedOn = lastUpdatedOn;
        this.status = status;
    }
}
