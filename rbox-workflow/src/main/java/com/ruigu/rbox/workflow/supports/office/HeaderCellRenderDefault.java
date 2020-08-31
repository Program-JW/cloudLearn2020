package com.ruigu.rbox.workflow.supports.office;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;

/**
 * 表头渲染器默认实现
 * @author alan.zhao
 */
public class HeaderCellRenderDefault implements HeaderCellRender {

    @Override
    public void updateHeaderStyle(XSSFWorkbook workbook, XSSFSheet sheet, XSSFCell cell, int index) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setColor(Font.COLOR_NORMAL);
        style.setWrapText(true);
        style.setFont(font);
        cell.setCellStyle(style);
    }
}
