package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author liqingtian
 * @date 2019-11-02 21:35:18
 */
@Data
public class StockChangeLogEntity implements Serializable {

	/**
	 * id
	 */
	private Long id;

	/**
	 * 库存变更表id
	 */
	private Integer applyId;

	/**
	 * 调用stock请求参数（json）
	 */
	private String requestData;

	/**
	 * stock返回参数（json）
	 */
	private String responseData;

	/**
	 * 创建时间
	 */
	private Date createOn;

	/**
	 * 日志状态 0无效 1有效
	 */
	private Integer status;

}
