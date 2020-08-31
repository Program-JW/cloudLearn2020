package com.ruigu.rbox.workflow.model.request.lightning;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author caojinghong
 * @date 2019/12/27 14:24
 */
@Data
public class LightningIssueApplyReq {

    /**
     * 问题所属分类ID
     */
    @ApiModelProperty(value = "问题所属分类ID", name = "categoryId", required = true)
    @NotNull(message = "问题所属分类ID不能为空")
    private Integer categoryId;

    /**
     * 问题描述
     */
    @ApiModelProperty(value = "问题描述", name = "description", required = true)
    @NotBlank(message = "问题描述不能为空")
    private String description;

    /**
     * 期望处理人
     */
    @ApiModelProperty(value = "期望处理人集合", name = "expectedSolver", required = true)
    @NotEmpty(message = "期望处理人ID不能为空")
    private List<Integer> expectedSolver;
    /**
     * 附件清单，逗号分隔的文件列表
     */
    @ApiModelProperty(value = "问题图片集合", name = "attachments", required = true)
    private List<String> attachments;
}
