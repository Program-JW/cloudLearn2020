package com.ruigu.rbox.workflow.supports;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author heyi
 * @date 2020/2/10 17:01
 */
public class ExportUtil {
    private static final String BROWSER_FIREFOX = "firefox";
    private static final String EXCEL_SUFFIX = ".xlsx";

    public static <T> void writeEasyExcelResponse(String fileName, List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            throw new VerificationFailedException(500, "暂无数据");
        }
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null) {
                throw new GlobalRuntimeException(500, "获取参数失败");
            }
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
            if (response == null) {
                throw new GlobalRuntimeException(500, "未获取的返回导致失败");
            }
            String agent = request.getHeader("USER-AGENT").toLowerCase();
            String codedFileName = getName(agent, fileName);
            response.setCharacterEncoding("utf-8");
            if (agent.contains(BROWSER_FIREFOX)) {
                response.setContentType("application/octet-stream");
                response.setHeader("content-disposition", "attachment;filename=" + codedFileName + EXCEL_SUFFIX);
            } else {
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("content-disposition", "attachment;filename=" + codedFileName + EXCEL_SUFFIX);
            }
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), list.get(0).getClass()).registerWriteHandler(new ExcelCustomHandler()).build();
            List<List<T>> lists = ListUtils.partition(list, 65535);
            for (int i = 0; i < lists.size(); i++) {
                WriteSheet writeSheet = EasyExcel.writerSheet(i, String.valueOf(i)).build();
                excelWriter.write(lists.get(i), writeSheet);
            }
            excelWriter.finish();
        } catch (IOException e) {
            e.printStackTrace();
            throw new GlobalRuntimeException(500, "IO失败导致批量导入失败");
        }
    }

    public static void writeBatchEasyExcelResponseOneSheet(HttpServletResponse response, HttpServletRequest request, OutputStream outputStream, String fileName, Class clazz, List<?> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            try {
                String agent = request.getHeader("USER-AGENT").toLowerCase();
                String codedFileName = getName(agent, fileName);
                response.setCharacterEncoding("utf-8");
                if (agent.contains(BROWSER_FIREFOX)) {
                    response.setContentType("application/octet-stream");
                    response.setHeader("content-disposition", "attachment;filename=" + codedFileName + EXCEL_SUFFIX);
                } else {
                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("content-disposition", "attachment;filename=" + codedFileName + EXCEL_SUFFIX);
                }

                ExcelWriter build = EasyExcel.write(outputStream).build();

                //一个sheet包含多个table情况
                WriteSheet writeSheet = new WriteSheet();

                WriteTable table1 = EasyExcel.writerTable(0).head(clazz).build();

                build.write(list, writeSheet, table1);

                build.finish();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getName(String agent, String filename) throws UnsupportedEncodingException {
        String name = filename + System.nanoTime();
        if (agent.contains(BROWSER_FIREFOX)) {
            // 火狐浏览器
            filename = new String(name.getBytes(), "ISO8859-1");
        } else {
            // 其它浏览器
            filename = URLEncoder.encode(name, "UTF-8");
        }
        return filename;
    }
}
