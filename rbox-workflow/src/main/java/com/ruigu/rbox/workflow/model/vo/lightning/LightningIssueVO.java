package com.ruigu.rbox.workflow.model.vo.lightning;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author caojinghong
 * @date 2019/12/31 11:37
 */
@Data
public class LightningIssueVO {
    @ApiModelProperty(value = "问题id", name = "id")
    private Integer id;
    /**
     * 问题标题
     */
    @ApiModelProperty(value = "问题标题", name = "title")
    private String title;

    /**
     * 问题描述
     */
    @ApiModelProperty(value = "问题描述", name = "description")
    private String description;

    /**
     * 创建人ID
     */
    @ApiModelProperty(value = "申请人id", name = "createdBy")
    private Integer createdBy;

    /**
     * 创建人名称(冗余字段)
     */
    @ApiModelProperty(value = "申请人名称", name = "creator")
    private String creator;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "发起申请时间", name = "createdOn")
    private Date createdOn;

    /**
     * 最后修改者
     */
    @ApiModelProperty(value = "最后修改者", name = "lastUpdatedBy")
    private Integer lastUpdatedBy;

    /**
     * 最后修改日期
     */
    @ApiModelProperty(value = "最后修改日期", name = "lastUpdatedOn")
    private Date lastUpdatedOn;

    /**
     *  状态： -1 作废 0 发起 1 待受理 2 受理中 3 待确认 4 已解决 5 未解决
     */
    @ApiModelProperty(value = "状态： -1 已撤销 0 发起 1 待受理 2 受理中 3 待确认 4 已解决 5 未解决", name = "status")
    private Integer status;

}
