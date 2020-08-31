package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author liqingtian
 * @date 2020/05/26 10:41
 */
@AllArgsConstructor
public enum StorageEnum {

    /**
     * 各仓对应code
     */

    EAST(455, "华东总仓"),

    CENTRAL(1649, "华中总仓"),

    SOUTH(1878, "华南总仓");

    @Getter
    private Integer code;
    @Getter
    private String value;

    public static String getValueByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (StorageEnum storage : StorageEnum.values()) {
            if (storage.code.equals(code)) {
                return storage.value;
            }
        }
        return null;
    }
}
