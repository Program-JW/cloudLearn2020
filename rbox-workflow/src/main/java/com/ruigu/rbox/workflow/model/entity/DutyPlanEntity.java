package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * 
 * @author liqingtian
 * @date 2020-05-07 14:20:08
 */
@Data
@Entity
@Table(name = "duty_plan")
public class DutyPlanEntity implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	/**
	 * 主键
	 */
	@Column(name = "id" ,columnDefinition= "int" )
	private Integer id;

	/**
	 * 所属规则ID
	 */
	@Column(name = "rule_id" ,columnDefinition= "int" )
	private Integer ruleId;

	/**
	 * 值班人
	 */
	@Column(name = "person_id" ,columnDefinition= "int" )
	private Integer personId;

	/**
	 * 值班人姓名
	 */
	@Column(name = "person_name" ,columnDefinition= "varchar" )
	private String personName;

	/**
	 * 值班日期
	 */
	@Column(name = "duty_date" ,columnDefinition= "timestamp" )
	private LocalDateTime dutyDate;

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
}
