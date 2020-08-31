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
@Table(name = "lightning_timeout_strategy_config")
public class LightningTimeoutStrategyConfigEntity implements Serializable {


	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id" ,columnDefinition= "int" )
	private Integer id;

	/**
	 * 所属策略ID
	 */
	@Column(name = "strategy_id" ,columnDefinition= "int" )
	private Integer strategyId;

	/**
	 * 问题优先级
	 */
	@Column(name = "issue_priority" ,columnDefinition= "int" )
	private Integer issuePriority;

	/**
	 * 升级级别
	 */
	@Column(name = "escalate_level" ,columnDefinition= "int" )
	private Integer escalateLevel;

	/**
	 * 类型 1未受理超时 2 未完成超时
	 */
	@Column(name = "type" ,columnDefinition= "int" )
	private Integer type;

	/**
	 * 超时时间 单位小时
	 */
	@Column(name = "timeout" ,columnDefinition= "int" )
	private Integer timeout;

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
	 * 状态： -1 删除 0 禁用 1 启用
	 */
	@Column(name = "status" ,columnDefinition= "int" )
	private Integer status;

}
