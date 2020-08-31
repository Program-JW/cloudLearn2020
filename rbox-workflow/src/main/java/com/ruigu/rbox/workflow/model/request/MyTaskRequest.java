package com.ruigu.rbox.workflow.model.request;

import com.ruigu.rbox.workflow.model.entity.BusinessParamEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author liqingtian
 * @date 2019/08/26 16:41
 */
@Setter
@ApiModel(value = "搜索我的任务参数对象", description = "搜索我的任务参数对象")
public class MyTaskRequest {

    /**
     * 关键字（用于搜索组合字段）
     */
    @ApiModelProperty(value = "关键字")
    private String keyWord;

    public String getKeyWord() {
        if (StringUtils.isBlank(keyWord)) {
            return null;
        }
        return "%" + keyWord + "%";
    }

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private Date begin;

    public Date getBegin() {
        return begin;
    }

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
    private Date end;

    public Date getEnd() {
        return end;
    }

    /**
     * 状态 0未处理 1开始处理 2处理完成 3审批 4驳回
     */
    @ApiModelProperty(value = "任务状态")
    private List<Integer> status;

    public List<Integer> getStatus() {
        if (CollectionUtils.isEmpty(status)) {
            return null;
        }
        return status;
    }

    /**
     * 分页
     */
    @ApiModelProperty(value = "第几页")
    private Integer pageNum;

    public Integer getPageNum() {
        if (pageNum == null || pageNum < 1) {
            return 0;
        } else {
            return pageNum - 1;
        }
    }

    /**
     * 流程定义id
     */
    @ApiModelProperty(value = "流程ID")
    private String definitionId;

    public String getDefinitionId() {
        if (StringUtils.isBlank(definitionId)) {
            return null;
        }
        return definitionId;
    }

    /**
     * 流程key
     */
    @ApiModelProperty(value = "流程key")
    private String definitionCode;

    public String getDefinitionCode() {
        if (StringUtils.isBlank(definitionCode)) {
            return null;
        }
        return definitionCode;
    }

    /**
     * 业务参数
     */
    @ApiModelProperty(value = "业务扩展参数列表")
    private List<BusinessParamEntity> businessParams;

    public List<BusinessParamEntity> getBusinessParams() {
        return businessParams;
    }
}
