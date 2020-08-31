package com.ruigu.rbox.workflow.model.request;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author liqingtian
 * @date 2019/10/16 16:39
 */
@Setter
public class OperationLogRequest {

    /**
     * 流程定义id
     */
    private String definitionId;

    public String getDefinitionId() {
        if (StringUtils.isBlank(definitionId)) {
            return null;
        }
        return definitionId;
    }

    private String instanceId;

    public String getInstanceId() {
        if (StringUtils.isBlank(instanceId)) {
            return null;
        }
        return instanceId;
    }

    private String taskId;

    public String getTaskId() {
        if (StringUtils.isBlank(taskId)) {
            return null;
        }
        return taskId;
    }

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    private Date begin;

    public Date getBegin() {
        return begin;
    }

    private Date end;

    public Date getEnd() {
        return end;
    }

    private Integer createdBy;

    public Integer getCreatedBy() {
        return createdBy;
    }

    private String content;

    public String getContent() {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        return "%" + content + "%";
    }

    private Integer pageNum;

    public Integer getPageNum() {
        if (pageNum == null || pageNum < 1) {
            return 0;
        }
        return pageNum - 1;
    }

    private Integer pageSize;
    
    public Integer getPageSize() {
        if (pageSize == null || pageSize < 1) {
            return 20;
        }
        return pageSize;
    }
}
