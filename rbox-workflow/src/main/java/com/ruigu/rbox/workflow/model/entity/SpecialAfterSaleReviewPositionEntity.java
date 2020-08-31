package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * 
 * @author chenzhenya
 * @date 2020-08-11 14:46:05
 */
@Data
@Entity
@Table(name = "special_after_sale_review_position")
public class SpecialAfterSaleReviewPositionEntity implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	/**
	 * 主键
	 */
	@Column(name = "id" ,columnDefinition= "int" )
	private Integer id;

	/**
	 * 审核配置ID
	 */
	@Column(name = "config_id" ,columnDefinition= "int" )
	private Integer configId;

	/**
	 * 职位
	 */
	@Column(name = "position" ,columnDefinition= "varchar" )
	private String position;

	/**
	 * 创建时间
	 */
	@Column(name = "created_at" ,columnDefinition= "datetime" )
	private LocalDateTime createdAt;

}
