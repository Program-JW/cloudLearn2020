package com.ruigu.rbox.workflow.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 *
 * @author yuanlin
 */
public class TableDataVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 总记录数
     */
    private long total;
    /**
     * 列表数据
     */
    private List<?> rows;
    /**
     * 消息状态码
     */
    @JsonIgnore
    private int code;

    /**
     * 表格数据对象
     */
    public TableDataVo() {
    }

    public TableDataVo(long total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }

    public TableDataVo(long total, List<?> rows, int code) {
        this.total = total;
        this.rows = rows;
        this.code = code;
    }

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */

    public TableDataVo(List<?> list, long total) {
        this.rows = list;
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
