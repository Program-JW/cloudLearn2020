package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * 
 * @author cg1
 * @date 2020-08-14 19:16:14
 */
@Data
@Entity
@Table(name = "special_after_sale_quota_rule_history")
public class SpecialAfterSaleQuotaRuleHistoryEntity implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	/**
	 * 主键
	 */
	@Column(name = "id" ,columnDefinition= "int" )
	private Integer id;

	/**
	 * 原始规则ID
	 */
	@Column(name = "rule_id" ,columnDefinition= "int" )
	private Integer ruleId;

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

}
