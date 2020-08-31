package com.ruigu.rbox.workflow.model.vo.lightning;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import lombok.ToString;

/**
 * @author caojinghong
 * @date 2020/01/10 14:56
 */
@Data
@ColumnWidth(20)
@ExcelIgnoreUnannotated
@ToString
public class DepartmentIssueVO {
    @ExcelProperty(value = "问题一级部门")
    private String firstLevelDepartmentName;
    @ExcelProperty(value = "问题二级部门")
    private String secondLevelDepartmentName;
    @ExcelProperty(value = "二级部门问题数")
    private Integer secondLevelIssueCount;
}
