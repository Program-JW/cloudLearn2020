package com.ruigu.rbox.workflow.supports.office;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel 单元格渲染器接口
 * @author alan.zhao
 */
public interface BodyCellRender<T> {

    /**
     * 更新单元格
     * @param workbook XSSFWorkbook 实例
     * @param sheet XSSFSheet 实例
     * @param cell XSSFCell 实例
     * @param data 当前数据
     * @param fieldName 字段名
     * @param column 当前列
     * @param index 当前位置
     */
    public void updateBodyStyle(XSSFWorkbook workbook, XSSFSheet sheet, XSSFCell cell, T data, String fieldName, Column column, int index);

}
