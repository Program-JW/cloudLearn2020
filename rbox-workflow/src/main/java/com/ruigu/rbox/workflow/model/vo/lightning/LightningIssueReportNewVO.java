package com.ruigu.rbox.workflow.model.vo.lightning;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import lombok.ToString;

/**
 * @author caojinghong
 * @date 2020/01/09 21:26
 */
@Data
@ColumnWidth(20)
@ExcelIgnoreUnannotated
@ToString
public class LightningIssueReportNewVO {
    @ExcelProperty(value = "（一级）部门")
    private String firstLevelDepartmentName;
    @ExcelProperty(value = "（二级）部门")
    private String secondLevelDepartmentName;
    @ExcelProperty(value = "姓名")
    private String personName;
    @ExcelProperty(value = "申请问题数")
    private Integer applyIssueCount;
    @ExcelProperty(value = "撤销问题数")
    private Integer revockIssueCount;
    @ExcelProperty(value = "未撤销的受理问题数")
    private Integer acceptIssueCount;
    @ExcelProperty(value = "当前正在受理中的问题数")
    private Integer currentAcceptIssueCount;
    @ExcelProperty(value = "最满意受理数")
    private Integer consideredBestCount;
    @ExcelProperty(value = "确认解决问题数")
    private Integer finishAndConfirmCount;
    @ExcelProperty(value = "交接问题数")
    private Integer transferIssueCount;
    @ExcelProperty(value = "一级升级问题数（超过4小时）")
    private Integer firstLevelPromotedCount;
    @ExcelProperty(value = "二级升级问题数（超过24小时）")
    private Integer secondLevelPromotedCount;
    @ExcelProperty(value = "三级升级问题数（超过48小时）")
    private Integer thirdLevelPromotedCount;

}
