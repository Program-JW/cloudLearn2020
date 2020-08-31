package com.ruigu.rbox.workflow.supports.office;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 表头渲染器
 *
 * @author alan.zhao
 */
public interface HeaderCellRender {

    /**
     * 更新表头
     *
     * @param workbook XSSFWorkbook 实例
     * @param sheet    XSSFSheet 实例
     * @param cell     XSSFCell 实例
     * @param index    当前位置
     */
    public void updateHeaderStyle(XSSFWorkbook workbook, XSSFSheet sheet, XSSFCell cell, int index);

}
