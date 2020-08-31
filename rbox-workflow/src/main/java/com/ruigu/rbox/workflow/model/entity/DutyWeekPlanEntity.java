package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * 
 * @author liqingtian
 * @date 2020-06-05 10:44:27
 */
@Data
@Entity
@Table(name = "duty_week_plan")
public class DutyWeekPlanEntity implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	/**
	 * id
	 */
	@Column(name = "id" ,columnDefinition= "int" )
	private Integer id;

	/**
	 * 策略id
	 */
	@Column(name = "rule_id" ,columnDefinition= "int" )
	private Integer ruleId;

	/**
	 * 周几
	 */
	@Column(name = "day_of_week" ,columnDefinition= "int" )
	private Integer dayOfWeek;

	/**
	 * 值班人 （json数组）
	 */
	@Column(name = "user_ids" ,columnDefinition= "varchar" )
	private String userIds;

	/**
	 * 创建时间
	 */
	@Column(name = "created_on" ,columnDefinition= "datetime" )
	private LocalDateTime createdOn;

	/**
	 * 创建人
	 */
	@Column(name = "created_by" ,columnDefinition= "int" )
	private Integer createdBy;

	/**
	 * 最后更新时间
	 */
	@Column(name = "last_updated_on" ,columnDefinition= "datetime" )
	private LocalDateTime lastUpdatedOn;

	/**
	 * 最后更新人
	 */
	@Column(name = "last_updated_by" ,columnDefinition= "int" )
	private Integer lastUpdatedBy;

}
