package com.ruigu.rbox.workflow.supports;

/**
 * @author alan.zhao
 */
public interface ValidPlugin {
    /**
     * 验证
     *
     * @param field 参数名
     * @param value 参数值
     * @return
     */
    ValidResult doValid(String field, String value);
}
