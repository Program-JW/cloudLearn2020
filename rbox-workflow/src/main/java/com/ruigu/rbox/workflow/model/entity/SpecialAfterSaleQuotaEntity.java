package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.time.LocalDateTime;
/**
 * 
 * @author chenzhenya
 * @date 2020-08-13 17:49:27
 */
@Data
@Entity
@Table(name = "special_after_sale_quota")
public class SpecialAfterSaleQuotaEntity implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	/**
	 * 主键
	 */
	@Column(name = "id" ,columnDefinition= "int" )
	private Integer id;

	/**
	 * 年月yyyyMM
	 */
	@Column(name = "y_m" ,columnDefinition= "int" )
	private Integer yM;

	/**
	 * 类型 1 电销 2 直销
	 */
	@Column(name = "type" ,columnDefinition= "int" )
	private Integer type;

	/**
	 * 组类型 1 城市组 2 BDM组
	 */
	@Column(name = "group_type" ,columnDefinition= "int" )
	private Integer groupType;

	/**
	 * 城市组ID或者BDM组ID
	 */
	@Column(name = "group_id" ,columnDefinition= "int" )
	private Integer groupId;

	/**
	 * 上月大通GMV
	 */
	@Column(name = "common_gmv" ,columnDefinition= "decimal" )
	private BigDecimal commonGmv;

	/**
	 * 上月非大通GMV
	 */
	@Column(name = "non_common_gmv" ,columnDefinition= "decimal" )
	private BigDecimal nonCommonGmv;

	/**
	 * 大通系数
	 */
	@Column(name = "common_coefficient" ,columnDefinition= "decimal" )
	private BigDecimal commonCoefficient;

	/**
	 * 非大通系数
	 */
	@Column(name = "non_common_coefficient" ,columnDefinition= "decimal" )
	private BigDecimal nonCommonCoefficient;

	/**
	 * 额度
	 */
	@Column(name = "quota" ,columnDefinition= "decimal" )
	private BigDecimal quota;

	/**
	 * 创建时间
	 */
	@Column(name = "created_at" ,columnDefinition= "datetime" )
	private LocalDateTime createdAt;

}
