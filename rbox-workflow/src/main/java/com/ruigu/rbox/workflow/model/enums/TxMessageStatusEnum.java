package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2020/07/10 16:03
 */
@AllArgsConstructor
public enum TxMessageStatusEnum {

    /**
     * 待处理
     */
    PENDING(0),

    /**
     * 成功
     */
    SUCCESS(1),

    /**
     * 处理失败
     */
    FAIL(2),

    ;

    @Getter
    private final Integer status;
}
