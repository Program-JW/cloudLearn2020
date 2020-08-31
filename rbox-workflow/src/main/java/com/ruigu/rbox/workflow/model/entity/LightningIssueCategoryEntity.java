package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * 
 * @author liqingtian
 * @date 2020-05-07 14:19:16
 */
@Data
@Entity
@Table(name = "lightning_issue_category")
public class LightningIssueCategoryEntity implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	/**
	 * 主键
	 */
	@Column(name = "id" ,columnDefinition= "int" )
	private Integer id;

	/**
	 * 分类名称
	 */
	@Column(name = "name" ,columnDefinition= "varchar" )
	private String name;

	/**
	 * 处理人ID
	 */
	@Column(name = "user_id" ,columnDefinition= "int" )
	private Integer userId;

	/**
	 * 处理人名称
	 */
	@Column(name = "user_name" ,columnDefinition= "varchar" )
	private String userName;

	/**
	 * 绑定的规则ID
	 */
	@Column(name = "rule_id" ,columnDefinition= "int" )
	private Integer ruleId;

	/**
	 * 排序
	 */
	@Column(name = "sort" ,columnDefinition= "int" )
	private Integer sort;

	/**
	 * 创建人ID
	 */
	@Column(name = "created_by" ,columnDefinition= "int" )
	private Integer createdBy;

	/**
	 * 创建时间
	 */
	@Column(name = "created_on" ,columnDefinition= "timestamp" )
	private LocalDateTime createdOn;

	/**
	 * 最后修改者
	 */
	@Column(name = "last_updated_by" ,columnDefinition= "int" )
	private Integer lastUpdatedBy;

	/**
	 * 最后修改日期
	 */
	@Column(name = "last_updated_on" ,columnDefinition= "timestamp" )
	private LocalDateTime lastUpdatedOn;

	/**
	 * 状态： -1 作废 0 禁用 1 启用
	 */
	@Column(name = "status" ,columnDefinition= "int" )
	private Integer status;

}
