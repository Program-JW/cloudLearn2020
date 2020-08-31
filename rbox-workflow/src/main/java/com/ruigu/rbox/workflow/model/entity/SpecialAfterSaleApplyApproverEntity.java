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
 * @date 2020-08-13 00:47:10
 */
@Data
@Entity
@Table(name = "special_after_sale_apply_approver")
public class SpecialAfterSaleApplyApproverEntity implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	/**
	 * id
	 */
	@Column(name = "id" ,columnDefinition= "bigint" )
	private Long id;

	/**
	 * apply_id
	 */
	@Column(name = "apply_id" ,columnDefinition= "bigint" )
	private Long applyId;

	/**
	 * 当前审批人
	 */
	@Column(name = "current_approver_id" ,columnDefinition= "int" )
	private Integer currentApproverId;

}
