package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * 
 * @author chenzhenya
 * @date 2020-08-10 15:38:17
 */
@Data
@Entity
@Table(name = "special_after_sale_cc")
public class SpecialAfterSaleCcEntity implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	/**
	 * 主键
	 */
	@Column(name = "id" ,columnDefinition= "bigint" )
	private Long id;

	/**
	 * 申请单ID
	 */
	@Column(name = "apply_id" ,columnDefinition= "bigint" )
	private Long applyId;

	/**
	 * 抄送人用户ID
	 */
	@Column(name = "user_id" ,columnDefinition= "int" )
	private Integer userId;

	/**
	 * 创建时间
	 */
	@Column(name = "created_at" ,columnDefinition= "datetime" )
	private LocalDateTime createdAt;

}
