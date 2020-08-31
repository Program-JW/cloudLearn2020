package com.ruigu.rbox.workflow.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * 可分页查询结果的返回结果基类
 *
 * @author xiangbohua
 * @date 2019-06-03 20:32
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageableRequest implements Serializable {
    /**
     * 默认页面大小
     */
    private final Integer defaultPageSize = 20;

    private Integer pageIndex;

    private Integer pageSize;

    public void setPageIndex(Integer index) {
        this.pageIndex = index;
    }

    public void setPageSize(Integer size) {
        this.pageSize = size;
    }

    public Integer getPageIndex() {
        if (this.pageIndex == null || this.pageIndex < 1) {
            this.pageIndex = 0;
            return this.pageIndex;
        }
        return this.pageIndex - 1;
    }

    public Integer getPageSize() {
        if (this.pageSize == null || this.pageSize < 0) {
            this.pageSize = this.defaultPageSize;
        }

        return this.pageSize;
    }
}
