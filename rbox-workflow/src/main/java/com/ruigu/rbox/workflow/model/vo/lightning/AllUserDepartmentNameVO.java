package com.ruigu.rbox.workflow.model.vo.lightning;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @author caojinghong
 * @date 2020/03/08 12:43
 */
@Data
@ColumnWidth(20)
@ExcelIgnoreUnannotated
public class AllUserDepartmentNameVO {
    @ExcelProperty(value = "用户id")
    private Integer userId;
    @ExcelProperty(value = "姓名")
    private String personName;
    @ExcelProperty(value = "（一级）部门")
    private String firstLevelDepartmentName;
    @ExcelProperty(value = "（二级）部门")
    private String secondLevelDepartmentName;
}
