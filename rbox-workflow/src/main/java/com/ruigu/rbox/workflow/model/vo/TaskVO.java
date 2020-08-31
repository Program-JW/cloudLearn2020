package com.ruigu.rbox.workflow.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author liqingtian
 * @date 2019/08/26 17:14
 */

@Data
@ApiModel(value = "用户所有任务查询结果实体")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskVO {
    @ApiModelProperty(value = "任务id",name = "id")
    private String id;
    @ApiModelProperty(value = "任务名称",name = "name")
    private String name;
    @ApiModelProperty(value = "流程实例ID",name = "instanceId")
    private String instanceId;
    @ApiModelProperty(value = "流程实例的名称",name = "instanceName")
    private String instanceName;
    @ApiModelProperty(value = "备注",name = "remarks")
    private String remarks;
    @ApiModelProperty(value = "状态：0未处理，1 处理中, 2 处理完成",name = "status")
    private Integer status;
    @ApiModelProperty(value = "创建时间",name = "createdOn")
    private Date createdOn;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人id",name = "createdBy")
    private Integer createdBy;
    @ApiModelProperty(value = "创建人名称",name = "creator")
    private String creator;

    /**
     * 流程创建人
     */
    @ApiModelProperty(value = "流程创建人id",name = "instanceCreatedBy")
    private BigInteger instanceCreatedBy;
    @ApiModelProperty(value = "流程创建人名称",name = "instanceCreator")
    private String instanceCreator;


    @ApiModelProperty(value = "任务开始时间",name = "beginTime")
    private Date beginTime;
    @ApiModelProperty(value = "任务提交时间",name = "submitTime")
    private Date submitTime;
    @ApiModelProperty(value = "提交人ID",name = "submitBy")
    private Long submitBy;
    @ApiModelProperty(value = "提交人名称",name = "submitter")
    private String submitter;

    @ApiModelProperty(value = "工作时间数,单位是分钟",name = "dueTime")
    private String dueTime;
    @ApiModelProperty(value = "候选用户,逗号分隔的id列表",name = "candidateUsers")
    private String candidateUsers;
    @ApiModelProperty(value = "候选用户组,逗号分隔的id列表",name = "candidateGroups")
    private String candidateGroups;
    @ApiModelProperty(value = "业务数据的唯一标识符",name = "orderNumber")
    private String orderNumber;
    @ApiModelProperty(value = "概要信息模板",name = "summary")
    private String summary;

    public TaskVO() {
    }

    public TaskVO(String instanceId, String instanceName) {
        this.instanceId = instanceId;
        this.instanceName = instanceName;
    }

    public TaskVO(String id, String name, String instanceId, String instanceName, Integer status, Date createdOn, Integer createdBy) {
        this.id = id;
        this.name = name;
        this.instanceId = instanceId;
        this.instanceName = instanceName;
        this.status = status;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
    }

    public TaskVO(String id, String name, Integer status, Date submitTime, Long submitBy, Date createdOn) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.submitTime = submitTime;
        this.submitBy = submitBy;
        this.createdOn = createdOn;
    }
}
