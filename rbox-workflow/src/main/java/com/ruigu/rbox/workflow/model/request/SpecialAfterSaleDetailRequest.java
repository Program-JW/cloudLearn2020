package com.ruigu.rbox.workflow.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author liqingtian
 * @date 2020/08/10 12:47
 */

@Data
public class SpecialAfterSaleDetailRequest {

    /**
     * 订单类型
     */
    @ApiModelProperty(value = "单号类型", name = "orderType", required = true)
    @NotNull(message = "单号类型不能为空")
    private Integer orderType;

    /**
     * 售后服务单号
     **/
    @ApiModelProperty(value = "单号", name = "orderNumber", required = true)
    @NotBlank(message = "单号不能为空")
    private String orderNumber;

    /**
     * 产品订购日期
     **/
    @ApiModelProperty(value = "产品订购日期", name = "orderDate", required = true)
    @NotNull(message = "产品订购日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime orderDate;

    /**
     * 申请售后日期
     **/
    @ApiModelProperty(value = "申请售后日期", name = "applyAfterSaleDate", required = true)
    @NotNull(message = "申请售后日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime applyAfterSaleDate;

    /**
     * 产品品牌
     **/
    @ApiModelProperty(value = "产品品牌", name = "productBrand", required = true)
    @NotBlank(message = "产品品牌不能为空")
    private String productBrand;

    /**
     * 产品名称
     **/
    @ApiModelProperty(value = "产品名称", name = "productName", required = true)
    @NotBlank(message = "产品名称不能为空")
    private String productName;

    /**
     * 产品编码
     **/
    @ApiModelProperty(value = "产品编码", name = "productNumber", required = true)
    @NotBlank(message = "产品编码不能为空")
    private String productNumber;

    /**
     * 产品数量
     **/
    @ApiModelProperty(value = "产品数量", name = "productCount", required = true)
    @NotNull(message = "产品数量不能为空")
    private Integer productCount;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额", name = "applyAmount", required = true)
    @NotNull(message = "金额不能为空")
    private BigDecimal applyAmount;
}
