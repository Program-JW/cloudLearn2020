package com.ruigu.rbox.workflow.supports.office;

import com.ruigu.rbox.workflow.supports.Reflections;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellType.STRING;

/**
 * excel 工具类
 *
 * @author alan.zhao
 */
public class ExcelUtils {
    private static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    public static void setCellStyle(XSSFWorkbook workbook, Cell cell, HorizontalAlignment align, boolean wrapped, boolean bold, IndexedColors color) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setColor(color.getIndex());
        font.setBold(bold);
        font.setFontName("宋体");
        style.setAlignment(align);
        style.setWrapText(wrapped);
        style.setFont(font);
        cell.setCellStyle(style);
    }

    public static String getCellValue(Cell c) {
        String o = null;
        if (c == null) {
            return null;
        }
        switch (c.getCellType()) {
            case BLANK:
                o = "";
                break;
            case BOOLEAN:
                o = String.valueOf(c.getBooleanCellValue());
                break;
            case FORMULA:
                o = String.valueOf(c.getCellFormula());
                break;
            case NUMERIC:
                o = String.valueOf(c.getDateCellValue());
                break;
            case STRING:
                o = c.getStringCellValue();
                break;
            default:
                o = null;
                break;
        }
        return o;
    }

    public static <T> XSSFWorkbook writeExcel(List<T> list, List<Column> columns, HeaderCellRender header, BodyCellRender<T> body) throws Exception {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("sheet1");
        XSSFRow row = sheet.createRow((int) 0);
        if (header == null) {
            header = new HeaderCellRenderDefault();
        }
        if (body == null) {
            body = new BodyCellRenderDefault<>();
        }
        BodyCellRender<T> defaultBody = new BodyCellRenderDefault<T>();

        if (columns != null && columns.size() > 0) {
            for (int z = 0; z < columns.size(); z++) {
                String fieldRealName = columns.get(z).getTitle();
                XSSFCell cell = row.createCell(z);
                cell.setCellValue(fieldRealName);
                if (header != null) {
                    header.updateHeaderStyle(wb, sheet, cell, z);
                }
                sheet.setColumnWidth(z, 512 * 20);
            }
        }
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                row = sheet.createRow((int) i + 1);
                T obj = list.get(i);
                Class<?> c = obj.getClass();
                for (int k = 0; k < columns.size(); k++) {
                    String fieldName = columns.get(k).getName();
                    XSSFCell cell = row.createCell(k);
                    if (cell != null) {
                        defaultBody.updateBodyStyle(wb, sheet, cell, obj, fieldName, columns.get(k), k);
                        if (body != null) {
                            body.updateBodyStyle(wb, sheet, cell, obj, fieldName, columns.get(k), k);
                        }
                    }
                }
            }
        }
        if (columns != null && columns.size() > 0) {
            for (int z = 0; z < columns.size(); z++) {
                sheet.autoSizeColumn((short) z);
            }
            setSizeColumn(sheet, columns.size());
        }
        return wb;
    }

    private static void setSizeColumn(XSSFSheet sheet, int size) {
        for (int columnNum = 0; columnNum < size; columnNum++) {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                XSSFRow currentRow;
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
                if (currentRow.getCell(columnNum) != null) {
                    XSSFCell currentCell = currentRow.getCell(columnNum);
                    if (STRING.equals(currentCell.getCellType())) {
                        int length = currentCell.getStringCellValue().getBytes(StandardCharsets.UTF_8).length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            sheet.setColumnWidth(columnNum, columnWidth * 256);
        }
    }

    public static <T> File writeExcel2File(String filename, Workbook wb) throws Exception {
        File file = new File(filename);
        FileOutputStream outputStream = new FileOutputStream(file);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
        return file;
    }

    public static String tempFile(HttpServletRequest request, String fileName) {
        String name = request.getSession().getServletContext().getRealPath("/") + "temp" + File.separator + String.valueOf(System.currentTimeMillis());
        File dtnFile = new File(name);
        String[] paths = dtnFile.getPath().split("\\\\" + File.separator);
        StringBuffer fullPath = new StringBuffer();
        for (int i = 0; i < paths.length; i++) {
            fullPath.append(paths[i]).append(File.separator);
            File file = new File(fullPath.toString());
            if (paths.length != i) {
                if (!file.exists()) {
                    file.mkdir();
                }
            }
        }
        return dtnFile.getPath() + File.separator + fileName;
    }

    private final static String XLS = "xls";
    private final static String XLSX = "xlsx";

    /**
     * 读入excel文件，解析后返回
     *
     * @param file
     * @throws IOException
     */
    public static List<String[]> readExcel(MultipartFile file) throws IOException {
        //检查文件
        checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<String[]> list = new ArrayList<String[]>();
        if (workbook != null) {
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if (sheet == null) {
                    continue;
                }
                //获得当前sheet的开始行
                int firstRowNum = sheet.getFirstRowNum();
                //获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                //循环除了第一行的所有行
                for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
                    //获得当前行的开始列
                    int firstCellNum = row.getFirstCellNum();
                    //获得当前行的列数
                    int lastCellNum = row.getPhysicalNumberOfCells();
                    String[] cells = new String[row.getPhysicalNumberOfCells()];
                    //循环当前行
                    for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        cells[cellNum] = getValue(cell);
                    }
                    list.add(cells);
                }
            }
            workbook.close();
        }
        return list;
    }

    /**
     * 读入excel文件，解析后返回
     *
     * @param file
     * @throws IOException
     */
    public static <T> List<T> readFromFile(File file, Class<T> cls, String[] fields) throws IOException {
        //检查文件
        checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<T> list = new ArrayList<>();
        if (workbook != null) {
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if (sheet == null) {
                    continue;
                }
                //获得当前sheet的开始行
                int firstRowNum = sheet.getFirstRowNum();
                //获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                int lastCellNum = sheet.getRow(0).getPhysicalNumberOfCells();
                //循环除了第一行的所有行
                for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
                    //获得当前行的开始列
                    int firstCellNum = row.getFirstCellNum();
                    //循环当前行
                    T item = Reflections.newInstance(cls);
                    for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        String value = getValue(cell);
                        if (StringUtils.isNotBlank(value)) {
                            Reflections.setFieldValue(item, fields[cellNum], value);
                        }
                    }
                    list.add(item);
                }
            }
            workbook.close();
        }
        return list;
    }

    public static void checkFile(MultipartFile file) throws IOException {
        //判断文件是否存在
        if (null == file) {
            logger.error("文件不存在！");
            throw new FileNotFoundException("文件不存在！");
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if (!fileName.endsWith(XLS) && !fileName.endsWith(XLSX)) {
            logger.error(fileName + "不是excel文件");
            throw new IOException(fileName + "不是excel文件");
        }
    }

    public static void checkFile(File file) throws IOException {
        //判断文件是否存在
        if (null == file) {
            logger.error("文件不存在！");
            throw new FileNotFoundException("文件不存在！");
        }
        //获得文件名
        String fileName = file.getName();
        //判断文件是否是excel文件
        if (!fileName.endsWith(XLS) && !fileName.endsWith(XLSX)) {
            logger.error(fileName + "不是excel文件");
            throw new IOException(fileName + "不是excel文件");
        }
    }

    public static Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith(XLS)) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(XLSX)) {
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return workbook;
    }

    public static Workbook getWorkBook(File file) {
        //获得文件名
        String fileName = file.getName();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = new FileInputStream(file);
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith(XLS)) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(XLSX)) {
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return workbook;
    }

    public static String getValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //判断数据的类型
        switch (cell.getCellType()) {
            case NUMERIC:
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case STRING:
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case BLANK:
                cellValue = "";
                break;
            case ERROR:
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }
}
