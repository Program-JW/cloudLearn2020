package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chenzhenya
 * @date 2020/7/31 16:55
 */
@Getter
@AllArgsConstructor
public enum PositionEnum {

    /**
     * 职位枚举
     */

    BD("BD"),
    BDM("BDM"),
    CM("CM"),
    DX("电销专员"),
    DXM("电销主管");

    private final String position;
}
