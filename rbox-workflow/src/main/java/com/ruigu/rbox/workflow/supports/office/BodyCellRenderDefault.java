package com.ruigu.rbox.workflow.supports.office;

import com.ruigu.rbox.workflow.supports.DateUtil;
import com.ruigu.rbox.workflow.supports.Reflections;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

/**
 * 表格单元格默认渲染器
 *
 * @param <T> 数据类
 * @author alan.zhao
 */
public class BodyCellRenderDefault<T> implements BodyCellRender<T> {

    public static Object parseParam(Object param, String key) {
        Object value;
        if (param instanceof HashMap) {
            value = ((HashMap<String, Object>) param).get(key);
        } else {
            value = Reflections.getFieldValue(param, key);
            if (value == null) {
                value = Reflections.invokeGetter(param, key);
            }
        }
        return value;
    }


    public void applyFormat(XSSFWorkbook workbook, XSSFSheet sheet, XSSFCell cell, T data, String fieldName, Column column, Object value) {
        if (value instanceof Integer || value instanceof Long || value instanceof Short) {
            if (StringUtils.isNotBlank(column.getFormat())) {
                CellStyle cellStyle = workbook.createCellStyle();
                XSSFDataFormat format = workbook.createDataFormat();
                cellStyle.setDataFormat(format.getFormat(column.getFormat()));
                cell.setCellStyle(cellStyle);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(Long.valueOf(value.toString()) * 1.0 / 60 / 60 / 24);
            } else {
                cell.setCellValue(Long.valueOf(value.toString()));
            }
        }
    }

    @Override
    public void updateBodyStyle(XSSFWorkbook workbook, XSSFSheet sheet, XSSFCell cell, T data, String fieldName, Column column, int index) {
        if (column.isVirtual() || cell == null) {
            return;
        }
        Object value = parseParam(data, fieldName);
        if (value == null) {
            return;
        }
        if (value instanceof Date) {
            String dateFormat = "yyyy-MM-dd HH:mm:ss";
            String dateTemp = DateUtil.formatDate((Date) value, dateFormat);
            cell.setCellValue(dateTemp);
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(new java.text.DecimalFormat("#,##0.##").format((BigDecimal) value));
            cell.setCellType(CellType.NUMERIC);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
            cell.setCellType(CellType.BOOLEAN);
        } else if (value instanceof Integer) {
            applyFormat(workbook, sheet, cell, data, fieldName, column, value);
        } else if (value instanceof Long) {
            applyFormat(workbook, sheet, cell, data, fieldName, column, value);
        } else if (value instanceof Short) {
            applyFormat(workbook, sheet, cell, data, fieldName, column, value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
            cell.setCellType(CellType.NUMERIC);
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
            cell.setCellType(CellType.NUMERIC);
        } else {
            cell.setCellValue(value.toString());
        }

    }
}
