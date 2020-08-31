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
 * @author cg1
 * @date 2020-08-11 17:11:36
 */
@Data
@Entity
@Table(name = "special_after_sale_review_node")
public class SpecialAfterSaleReviewNodeEntity implements Serializable {

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id" ,columnDefinition= "int" )
	private Integer id;

	/**
	 * 审核配置ID
	 */
	@Column(name = "config_id" ,columnDefinition= "int" )
	private Integer configId;

	/**
	 * 节点名称
	 */
	@Column(name = "name" ,columnDefinition= "varchar" )
	private String name;

	/**
	 * 审核顺序
	 */
	@Column(name = "sort" ,columnDefinition= "int" )
	private Integer sort;

	/**
	 * 标志,1上级按职位,2单人
	 */
	@Column(name = "flag" ,columnDefinition= "tinyint" )
	private Integer flag;

	/**
	 * 是否扣额度 1 是 2 否
	 */
	@Column(name = "use_quota" ,columnDefinition= "int" )
	private Integer useQuota;

	/**
	 * 审核人ID,只在flag=2有效
	 */
	@Column(name = "user_id" ,columnDefinition= "int" )
	private Integer userId;

	/**
	 * 候选人职位,多个职位逗号分隔
	 */
	@Column(name = "positions" ,columnDefinition= "varchar" )
	private String positions;

	/**
	 * 创建时间
	 */
	@Column(name = "created_at" ,columnDefinition= "datetime" )
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime createdAt;

	/**
	 * 最后修改时间
	 */
	@Column(name = "last_update_at" ,columnDefinition= "datetime" )
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime lastUpdateAt;

	/**
	 * 创建人
	 */
	@Column(name = "created_by" ,columnDefinition= "int" )
	private Integer createdBy;

	/**
	 * 最后更新人
	 */
	@Column(name = "last_update_by" ,columnDefinition= "int" )
	private Integer lastUpdateBy;

	/**
	 * 状态：0 禁用 1 启用
	 */
	@Column(name = "status" ,columnDefinition= "tinyint" )
	private Integer status;

}
