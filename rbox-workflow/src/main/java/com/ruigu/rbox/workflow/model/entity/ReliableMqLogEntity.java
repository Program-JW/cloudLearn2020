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
 * @date 2020-07-21 10:22:33
 */
@Data
@Entity
@Table(name = "reliable_mq_log")
public class ReliableMqLogEntity implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id" ,columnDefinition= "bigint unsigned" )
	private Long id;

	/**
	 * 当前重试次数
	 */
	@Column(name = "current_retry_times" ,columnDefinition= "int" )
	private Integer currentRetryTimes;

	/**
	 * 最大重试次数
	 */
	@Column(name = "max_retry_times" ,columnDefinition= "int" )
	private Integer maxRetryTimes;

	/**
	 * 交换器名
	 */
	@Column(name = "exchange_name" ,columnDefinition= "varchar" )
	private String exchangeName;

	/**
	 * 交换类型
	 */
	@Column(name = "exchange_type" ,columnDefinition= "varchar" )
	private String exchangeType;

	/**
	 * 路由键
	 */
	@Column(name = "routing_key" ,columnDefinition= "varchar" )
	private String routingKey;

	/**
	 * 消息内容
	 */
	@Column(name = "content" ,columnDefinition= "text" )
	private String content;

	/**
	 * 业务模块
	 */
	@Column(name = "business_module" ,columnDefinition= "varchar" )
	private String businessModule;

	/**
	 * 业务键
	 */
	@Column(name = "business_key" ,columnDefinition= "varchar" )
	private String businessKey;

	/**
	 * 下一次调度时间
	 */
	@Column(name = "next_schedule_time" ,columnDefinition= "datetime" )
	private LocalDateTime nextScheduleTime;

	/**
	 * 消息状态
	 */
	@Column(name = "message_status" ,columnDefinition= "tinyint" )
	private Integer messageStatus;

	/**
	 * 退避初始化值,单位为秒
	 */
	@Column(name = "init_backoff" ,columnDefinition= "bigint unsigned" )
	private Long initBackoff;

	/**
	 * 退避因子(也就是指数)
	 */
	@Column(name = "backoff_factor" ,columnDefinition= "tinyint" )
	private Integer backoffFactor;

	@Column(name = "created_at" ,columnDefinition= "datetime" )
	private LocalDateTime createdAt;

	@Column(name = "last_updated_at" ,columnDefinition= "datetime" )
	private LocalDateTime lastUpdatedAt;

	@Column(name = "created_by" ,columnDefinition= "int" )
	private Integer createdBy;

	@Column(name = "last_updated_by" ,columnDefinition= "int" )
	private Integer lastUpdatedBy;
}
