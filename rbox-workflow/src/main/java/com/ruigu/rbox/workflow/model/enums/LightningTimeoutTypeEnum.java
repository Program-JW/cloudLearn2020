package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2020/02/26 13:40
 */
@AllArgsConstructor
public enum LightningTimeoutTypeEnum {

    /**
     * 未受理
     */
    NOT_ACCEPTED(1),

    /**
     * 未完成
     */
    INCOMPLETE(2);

    @Getter
    private Integer code;
}
