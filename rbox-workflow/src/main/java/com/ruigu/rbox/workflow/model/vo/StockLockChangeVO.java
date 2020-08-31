package com.ruigu.rbox.workflow.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xuhaoxiang
 * @date 2020/7/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockLockChangeVO {
    private Integer approvedStatus;
    private Integer applyId;
}
