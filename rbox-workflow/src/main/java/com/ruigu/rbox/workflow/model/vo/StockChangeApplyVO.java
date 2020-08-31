package com.ruigu.rbox.workflow.model.vo;

import lombok.Data;

/**
 * @author liqingtian
 * @date 2019/11/02 21:32
 */
@Data
public class StockChangeApplyVO {

    private Long id;
    private Long storageId;
    private Integer stockType;
    private String creator;
    private Long skuCode;
    private Integer count;

}
