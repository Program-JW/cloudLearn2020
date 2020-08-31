package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * 
 * @author liqingtian
 * @date 2020-02-26 09:51:56
 */
@Data
@Entity
@Table(name = "lightning_timeout_strategy")
public class LightningTimeoutStrategyEntity implements Serializable {


	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id" ,columnDefinition= "int" )
	private Integer id;

	/**
	 * 策略名称
	 */
	@Column(name = "name" ,columnDefinition= "varchar" )
	private String name;

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
	 * 状态： -1 删除 0 禁用 1 启用
	 */
	@Column(name = "status" ,columnDefinition= "int" )
	private Integer status;

}
