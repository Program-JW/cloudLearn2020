package com.ruigu.rbox.workflow.model.request;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 搜索流程实例请求
 * @author alan.zhao
 */
@Setter
public class SearchInstanceRequest {
    private String name;
    private String definitionId;
    private Integer status;
    private Date begin;
    private Date end;
    private Integer pageIndex;
    private Integer pageSize;
    /**
     * 0 历史 1 运行
     */
    @NotNull(message = "请选择是否为历史流程")
    private Integer history;

    public String getName() {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return name;
    }

    public String getDefinitionId() {
        if (StringUtils.isBlank(definitionId)) {
            return null;
        }
        return definitionId;
    }

    public Integer getStatus() {
        return status;
    }

    public Date getBegin() {
        return begin;
    }

    public Date getEnd() {
        return end;
    }

    public Integer getPageIndex() {
        if (pageIndex == null || pageIndex < 1) {
            return 0;
        }
        return pageIndex - 1;
    }

    public Integer getPageSize() {
        if (pageSize == null || pageSize < 1) {
            return 20;
        }
        return pageSize;
    }

    public Integer getHistory() {
        return history;
    }
}
