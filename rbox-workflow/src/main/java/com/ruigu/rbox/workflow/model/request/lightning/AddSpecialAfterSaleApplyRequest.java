package com.ruigu.rbox.workflow.model.request.lightning;

import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleDetailRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddSpecialAfterSaleApplyRequest {

    @ApiModelProperty(value = "大通/非大通品牌", name = "common", required = true)
    @NotNull(message = "大通/非大通品牌不能为空")
    private Integer common;

    /**
     * 区域id
     */
    @ApiModelProperty(value = "区域id", name = "areaId", required = true)
    @NotNull(message = "区域id不能为空")
    private Integer areaId;

    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称", name = "areaName", required = true)
    @NotBlank(message = "区域名称不能为空")
    private String areaName;


    /**
     * 城市id
     */
    @ApiModelProperty(value = "城市id", name = "cityId", required = true)
    @NotNull(message = "城市id不能为空")
    private Integer cityId;

    /**
     * 城市名称
     */
    @ApiModelProperty(value = "城市名称", name = "cityName", required = true)
    @NotBlank(message = "城市名称不能为空")
    private String cityName;


    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id", name = "customerId", required = true)
    @NotNull(message = "客户id不能为空")
    private Integer customerId;


    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称", name = "customerName", required = true)
    @NotBlank(message = "客户名称不能为空")
    private String customerName;

    /**
     * 客户等级
     */
    @ApiModelProperty(value = "客户等级", name = "customerRating", required = true)
    @NotBlank(message = "客户等级不能为空")
    private String customerRating;

    /**
     * 客户电话
     */
    @ApiModelProperty(value = "客户电话", name = "customerPhone", required = true)
    @NotBlank(message = "客户电话不能为空")
    private String customerPhone;


    /**
     * 特殊售后申请原因
     */
    @ApiModelProperty(value = "申请原因", name = "applicationReasons", required = true)
    @NotBlank(message = "申请原因不能为空")
    private String applicationReasons;

    /**
     * 特批类型
     */
    @ApiModelProperty(value = "特批类型", name = "type", required = true)
    @NotNull(message = "特批类型不能为空")
    private Integer type;

    /**
     * 需要支持
     */
    @ApiModelProperty(value = "需要支持", name = "needSupport", required = true)
    @NotBlank(message = "需要支持不能为空")
    private String needSupport;

    @Valid
    @NotEmpty(message = "详情不能为空")
    List<SpecialAfterSaleDetailRequest> details;
}
