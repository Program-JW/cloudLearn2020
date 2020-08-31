package com.ruigu.rbox.workflow.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件上传响应
 *
 * @author alan.zhao
 */
@Data
@ApiModel(value = "FileUploadResponse", description = "文件上传响应数据")
public class FileUploadResponse {

    @ApiModelProperty(value = "相对路径")
    private String path;

    @ApiModelProperty(value = "完整访问地址")
    private String url;

    @ApiModelProperty(value = "文件名")
    private String name;

    @ApiModelProperty(value = "相对路径")
    private String thumbPath;

    @ApiModelProperty(value = "完整访问地址")
    private String thumbUrl;

    @ApiModelProperty(value = "文件名")
    private String thumbName;

}
