package com.ruigu.rbox.workflow.model.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author caojinghong
 * @date 2020/01/09 22:30
 */
@Data
@ColumnWidth(20)
@ExcelIgnoreUnannotated
@ToString
public class WholeReportDTO {
    @ExcelProperty(value = "问题总数（非撤销的）")
    private Integer issueCount;
    @ExcelProperty(value = "评价星星总数")
    private Integer evaluateScoreCount;
    @ExcelProperty(value = "被转化为需求的问题数")
    private Integer issueDemandCount;
}
