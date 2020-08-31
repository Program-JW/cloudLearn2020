package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.time.LocalDateTime;
/**
 * 
 * @author cg1
 * @date 2020-08-17 14:14:23
 */
@Data
@Entity
@Table(name = "special_after_sale_quota_rule")
public class SpecialAfterSaleQuotaRuleEntity implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	/**
	 * 主键
	 */
	@Column(name = "id" ,columnDefinition= "int" )
	private Integer id;

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
	 * 创建时间
	 */
	@Column(name = "created_at" ,columnDefinition= "datetime" )
	private LocalDateTime createdAt;

	/**
	 * 最后更新时间
	 */
	@Column(name = "last_update_at" ,columnDefinition= "datetime" )
	private LocalDateTime lastUpdateAt;

	/**
	 * 创建人
	 */
	@Column(name = "created_by" ,columnDefinition= "int" )
	private Integer createdBy;

	/**
	 * 最后修改人
	 */
	@Column(name = "last_update_by" ,columnDefinition= "int" )
	private Integer lastUpdateBy;

	/**
	 * 状态
	 */
	@Column(name = "status" ,columnDefinition= "tinyint" )
	private Integer status;

}
