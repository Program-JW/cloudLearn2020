package com.ruigu.rbox.workflow.supports;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 文件工具类
 *
 * @author alan.zhao
 */
@Slf4j
public class FileUtil {

    /**
     * 文件下载
     *
     * @param file     要下载的文件
     * @param filename 文件名称
     * @param request  HttpServletRequest实例
     * @param response HttpServletResponse实例
     */
    public static void download(File file,String filename, HttpServletRequest request, HttpServletResponse response) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            if (file.exists()) {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/force-download");
                String downFileName = filename;
                String agent = request.getHeader("User-Agent").toUpperCase();
                final String ie = "MSIE";
                final String firefox = "GECKO";
                final String rv = "RV:11";
                boolean utf8 = agent.indexOf(ie) > 0 || (agent.indexOf(firefox) > 0 && agent.indexOf(rv) > 0);
                if (utf8) {
                    downFileName = URLEncoder.encode(downFileName, "UTF-8");
                } else {
                    downFileName = new String(downFileName.getBytes("gb2312"), "ISO8859-1");
                }
                response.setHeader("Content-Disposition", "attachment;filename=" + downFileName);
                byte[] buffer = new byte[1024];
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error("", e);
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    log.error("", e);
                }
            }
        }
    }
}
