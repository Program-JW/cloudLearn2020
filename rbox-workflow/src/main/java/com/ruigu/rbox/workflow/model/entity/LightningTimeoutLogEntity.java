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
@Table(name = "lightning_timeout_log")
public class LightningTimeoutLogEntity implements Serializable {
	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id" ,columnDefinition= "int" )
	private Integer id;

	/**
	 * 问题id
	 */
	@Column(name = "issue_id" ,columnDefinition= "int" )
	private Integer issueId;

	/**
	 * 用户id(超时领导id)
	 */
	@Column(name = "user_id" ,columnDefinition= "int" )
	private Integer userId;

	/**
	 * 超时级别
	 */
	@Column(name = "level" ,columnDefinition= "int" )
	private Integer level;

	/**
	 * 超时小时数
	 */
	@Column(name = "timeout" ,columnDefinition= "int" )
	private Integer timeout;

	/**
	 * 类型 1.未完成超时 2.未受理超时
	 */
	@Column(name = "type" ,columnDefinition= "int" )
	private Integer type;

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
	 * 最后修改时间
	 */
	@Column(name = "last_updated_on" ,columnDefinition= "datetime" )
	private LocalDateTime lastUpdatedOn;

	/**
	 * 最后修改人
	 */
	@Column(name = "last_updated_by" ,columnDefinition= "int" )
	private Integer lastUpdatedBy;

	/**
	 * 状态
	 */
	@Column(name = "status" ,columnDefinition= "int" )
	private Integer status;

}
