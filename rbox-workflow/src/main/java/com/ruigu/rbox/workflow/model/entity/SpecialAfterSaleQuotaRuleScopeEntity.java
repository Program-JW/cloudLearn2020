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
@Table(name = "special_after_sale_quota_rule_scope")
public class SpecialAfterSaleQuotaRuleScopeEntity implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	/**
	 * 主键
	 */
	@Column(name = "id" ,columnDefinition= "int" )
	private Integer id;

	/**
	 * 规则ID
	 */
	@Column(name = "rule_id" ,columnDefinition= "int" )
	private Integer ruleId;

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
	 * 适应范围 1 电销 2 直销
	 */
	@Column(name = "type" ,columnDefinition= "int" )
	private Integer type;

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
