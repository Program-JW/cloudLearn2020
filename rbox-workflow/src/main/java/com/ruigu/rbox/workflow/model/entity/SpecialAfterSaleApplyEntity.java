package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * 
 * @author cg1
 * @date 2020-08-20 14:55:12
 */
@Data
@Entity
@Table(name = "special_after_sale_apply")
public class SpecialAfterSaleApplyEntity implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	/**
	 * 主键
	 */
	@Column(name = "id" ,columnDefinition= "bigint" )
	private Long id;

	/**
	 * 审批单号
	 */
	@Column(name = "code" ,columnDefinition= "varchar" )
	private String code;

	/**
	 * 流程定义ID
	 */
	@Column(name = "definition_id" ,columnDefinition= "varchar" )
	private String definitionId;

	/**
	 * 流程实例ID
	 */
	@Column(name = "instance_id" ,columnDefinition= "varchar" )
	private String instanceId;

	/**
	 * 审批配置ID
	 */
	@Column(name = "config_id" ,columnDefinition= "int" )
	private Integer configId;

	/**
	 * 是否大通 0-非大通 1-大通
	 */
	@Column(name = "is_common" ,columnDefinition= "int" )
	private Integer common;

	/**
	 * 申请单类型 1 退货 2 换货 3 补金币
	 */
	@Column(name = "type" ,columnDefinition= "int" )
	private Integer type;

	/**
	 * 申请人类型 1 bd 2 dx
	 */
	@Column(name = "apply_user_type" ,columnDefinition= "int" )
	private Integer applyUserType;

	/**
	 * 城市id
	 */
	@Column(name = "city_id" ,columnDefinition= "int" )
	private Integer cityId;

	/**
	 * 城市名称
	 */
	@Column(name = "city_name" ,columnDefinition= "varchar" )
	private String cityName;

	/**
	 * 区域id
	 */
	@Column(name = "area_id" ,columnDefinition= "int" )
	private Integer areaId;

	/**
	 * 区域名称
	 */
	@Column(name = "area_name" ,columnDefinition= "varchar" )
	private String areaName;

	/**
	 * 客户id
	 */
	@Column(name = "customer_id" ,columnDefinition= "int" )
	private Integer customerId;

	/**
	 * 客户名称
	 */
	@Column(name = "customer_name" ,columnDefinition= "varchar" )
	private String customerName;

	/**
	 * 客户电话
	 */
	@Column(name = "customer_phone" ,columnDefinition= "varchar" )
	private String customerPhone;

	/**
	 * 客户等级
	 */
	@Column(name = "customer_rating" ,columnDefinition= "varchar" )
	private String customerRating;

	/**
	 * 申请原因
	 */
	@Column(name = "apply_reason" ,columnDefinition= "text" )
	private String applyReason;

	/**
	 * 需要支持
	 */
	@Column(name = "need_support" ,columnDefinition= "varchar" )
	private String needSupport;

	/**
	 * 申请总金额
	 */
	@Column(name = "total_apply_amount" ,columnDefinition= "decimal" )
	private BigDecimal totalApplyAmount;

	/**
	 * 申请人姓名
	 */
	@Column(name = "apply_nickname" ,columnDefinition= "varchar" )
	private String applyNickname;

	/**
	 * 审批时间
	 */
	@Column(name = "approval_time" ,columnDefinition= "datetime" )
	private LocalDateTime approvalTime;

	/**
	 * 审批人id
	 */
	@Column(name = "approval_user_id" ,columnDefinition= "int" )
	private Integer approvalUserId;

	/**
	 * 状态： -1 作废 0 待审批 1 审批中 3 已通过 4 已驳回
	 */
	@Column(name = "status" ,columnDefinition= "tinyint" )
	private Integer status;

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
