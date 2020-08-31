package com.ruigu.rbox.workflow.supports;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;
import java.io.File;
import java.io.IOException;

@Slf4j
public class ThumbUtil {

    /**
     * @param originFile 源图片
     * @param size       生成的缩略图文件的大小
     * @param thumbFile  生成的缩略图文件
     * @throws IOException
     */
    public static void create(File originFile, int size, File thumbFile) throws IOException {
        try {
            String fileName = originFile.getName();
            String ext = fileName.substring(fileName.indexOf('.') + 1);
            if ("jpg".equalsIgnoreCase(ext) || "jpeg".equalsIgnoreCase(ext) || "png".equalsIgnoreCase(ext) || "gif".equalsIgnoreCase(ext) || "bmp".equalsIgnoreCase(ext)) {
                Thumbnails.of(originFile).size(size, size).toFile(thumbFile);
            }
        } catch (UnsupportedFormatException e) {
            log.error("文件不是图片文件", e);
        }
    }

}
