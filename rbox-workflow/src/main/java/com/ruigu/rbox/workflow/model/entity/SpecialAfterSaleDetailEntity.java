package com.ruigu.rbox.workflow.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author chenzhenya
 * @date 2020-08-10 15:38:17
 */
@Data
@Entity
@Table(name = "special_after_sale_detail")
public class SpecialAfterSaleDetailEntity implements Serializable {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint")
    @ApiModelProperty(value = "id", name = "id")
    private Long id;

    /**
     * 申请id
     */
    @Column(name = "apply_id", columnDefinition = "bigint")
    @ApiModelProperty(value = "申请id", name = "applyId")
    private Long applyId;

    /**
     * 订单类型
     */
    @Column(name = "order_type", columnDefinition = "int")
    @ApiModelProperty(value = "单号类型", name = "orderType", required = true)
    private Integer orderType;

    /**
     * 售后服务单号
     */
    @Column(name = "order_number", columnDefinition = "varchar")
    @ApiModelProperty(value = "售后服务单号", name = "orderNumber")
    private String orderNumber;

    /**
     * 商品订购日期
     */
    @Column(name = "order_date", columnDefinition = "datetime")
    @ApiModelProperty(value = "商品订购日期", name = "orderDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime orderDate;

    /**
     * 申请售后日期
     */
    @Column(name = "apply_after_sale_date", columnDefinition = "datetime")
    @ApiModelProperty(value = "申请售后日期", name = "applyAfterSaleDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime applyAfterSaleDate;

    /**
     * 产品品牌
     */
    @Column(name = "product_brand", columnDefinition = "varchar")
    @ApiModelProperty(value = "产品品牌", name = "productBrand")
    private String productBrand;

    /**
     * 产品名称
     */
    @Column(name = "product_name", columnDefinition = "varchar")
    @ApiModelProperty(value = "产品名称", name = "productName")
    private String productName;

    /**
     * 产品编码
     */
    @Column(name = "product_number", columnDefinition = "varchar")
    @ApiModelProperty(value = "产品编码", name = "productNumber")
    private String productNumber;

    /**
     * 产品数量
     */
    @Column(name = "product_count", columnDefinition = "int")
    @ApiModelProperty(value = "产品数量", name = "productCount")
    private Integer productCount;

    /**
     * 申请金额
     */
    @Column(name = "apply_amount", columnDefinition = "decimal")
    @ApiModelProperty(value = "申请金额", name = "applyAmount")
    private BigDecimal applyAmount;

}
