package com.ruigu.rbox.workflow.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * 
 * @author liqingtian
 * @date 2020-05-07 14:20:15
 */
@Data
@Entity
@Table(name = "duty_rule")
public class DutyRuleEntity implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	/**
	 * 主键
	 */
	@Column(name = "id" ,columnDefinition= "int" )
	private Integer id;

	/**
	 * 策略名称
	 */
	@Column(name = "name" ,columnDefinition= "varchar" )
	private String name;

	/**
	 * 使用范围标记 1. 产品技术专用
	 */
	@Column(name = "scope_type" ,columnDefinition= "int" )
	private Integer scopeType;

	/**
	 * 是否预定义的 1 是 0 否
	 */
	@Column(name = "is_pre_defined" ,columnDefinition= "int" )
	private Integer isPreDefined;

	/**
	 * 值班策略 1 按天轮流分配 2 按问题轮流分配
	 */
	@Column(name = "type" ,columnDefinition= "int" )
	private Integer type;

	/**
	 * 部门ID
	 */
	@Column(name = "department_id" ,columnDefinition= "int" )
	private Integer departmentId;

	/**
	 * 值班人配置. 对类型2有效
	 */
	@Column(name = "user_ids" ,columnDefinition= "text" )
	private String userIds;

	/**
	 * 创建人ID
	 */
	@Column(name = "created_by" ,columnDefinition= "int" )
	private Integer createdBy;

	/**
	 * 创建时间
	 */
	@Column(name = "created_on" ,columnDefinition= "timestamp" )
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime lastUpdatedOn;

	/**
	 * 状态：0 禁用 1 启用
	 */
	@Column(name = "status" ,columnDefinition= "int" )
	private Integer status;

}
