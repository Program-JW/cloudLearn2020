package com.ruigu.rbox.workflow.controller;

import com.UpYun;
import com.alibaba.fastjson.JSON;
import com.ruigu.rbox.workflow.config.UpyunConfig;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.response.FileUploadResponse;
import com.ruigu.rbox.workflow.supports.ThumbUtil;
import io.swagger.annotations.*;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author alan
 */
@Api(value = "文件服务", tags = {"文件服务"})
@RestController
@RequestMapping(value = "storage")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private UpyunConfig upyunConfig;

    @ApiOperation(value = "上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dir", value = "上传的根目录", dataType = "string", required = true),
            @ApiImplicitParam(name = "del", value = "是否删除临时文件", dataType = "int")
    })
    @PostMapping(value = "/upload")
    public ServerResponse<FileUploadResponse> upload(MultipartFile file,
                                                     @RequestParam(value = "dir", required = false) String dir,
                                                     @RequestParam(value = "size", required = false) String size,
                                                     @RequestParam(value = "del", required = false, defaultValue = "1") int del) {
        FileUploadResponse data = new FileUploadResponse();
        File originFile = null;
        File thumbFile = null;
        try {
            if (StringUtils.isBlank(dir)) {
                return ServerResponse.fail("dir参数不能为空，dir表示文件的相对目录");
            }
            String fileName = file.getOriginalFilename();
            String ext = fileName.substring(fileName.indexOf('.'));
            String imageName = UUID.randomUUID().toString();
            originFile = File.createTempFile(imageName, ext);
            if (!originFile.getParentFile().exists()) {
                originFile.getParentFile().mkdirs();
            }
            file.transferTo(originFile);
            if (StringUtils.isNotBlank(size)) {
                thumbFile = File.createTempFile("thumb", ext);
                ThumbUtil.create(originFile, Integer.parseInt(size), thumbFile);
            }
            String storePath = upyunConfig.getStorePath();
            String storeName = String.format("%s%s", imageName, ext);
            String thumbStoreName = String.format("%s_thumb%s", imageName, ext);
            String pathSeparator = "/";
            if (storePath.endsWith(pathSeparator)) {
                storePath = storePath.substring(0, storePath.length() - 1);
            }
            storePath = storePath + pathSeparator + dir;
            String viewPrefix = upyunConfig.getPrefix();
            if (viewPrefix.endsWith(pathSeparator)) {
                viewPrefix = viewPrefix.substring(0, viewPrefix.length() - 1);
            }
            UpYun upyun = new UpYun(upyunConfig.getBucketName(), upyunConfig.getUsername(), upyunConfig.getPassword());
            upyun.setContentMD5(UpYun.md5(originFile));
            boolean result = upyun.writeFile(storePath + pathSeparator + storeName, originFile, true);
            if (result) {
                data.setPath(storePath + pathSeparator + storeName);
                data.setUrl(viewPrefix + storePath + pathSeparator + storeName);
                data.setName(fileName);
                if (thumbFile != null) {
                    boolean result1 = upyun.writeFile(storePath + pathSeparator + thumbStoreName, thumbFile, true);
                    if (result1) {
                        data.setThumbPath(storePath + pathSeparator + thumbStoreName);
                        data.setThumbUrl(viewPrefix + storePath + pathSeparator + thumbStoreName);
                        data.setThumbName(thumbStoreName);
                    } else {
                        return ServerResponse.fail("上传到upyun失败");
                    }
                }
                System.out.println(JSON.toJSONString(data));
                return ServerResponse.ok(data);
            } else {
                return ServerResponse.fail("上传到upyun失败");
            }
        } catch (Exception e) {
            logger.error("", e);
            return ServerResponse.fail(e.getMessage());
        } finally {
            if (originFile != null && originFile.exists()) {
                if (del == 1) {
                    originFile.delete();
                }
            }
        }
    }
}
