package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author caojinghong
 * @date 2019/12/31 17:35
 */
@AllArgsConstructor
public enum LightningIssueLogStatusEnum {
    /**
     * 状态： -1 作废 1 启用 0 禁用
     */
    INVALID(-1, "作废"),

    OPEN(1, "启用"),

    CLOSED(0, "禁用");
    @Getter
    @Setter
    private Integer code;
    @Getter
    @Setter
    private String desc;


}
