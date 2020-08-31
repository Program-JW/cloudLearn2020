package com.ruigu.rbox.workflow.model.enums;

import lombok.Getter;

/**
 * http 请求 返回状态码
 *
 * @author alan.zhao
 */
public enum ResponseCode {
    /**
     *
     */
    SUCCESS(200, "请求成功"),
    ERROR(404, "ERROR"),
    LOGIC_ERROR(-1, "逻辑错误"),
    REQUEST_ERROR(400, "参数转换失败"),
    INTERNAL_ERROR(500, "内部服务错误"),
    METHOD_NOT_SUPPOTED(401, "请求方法不支持"),
    REFUSE_EXECUTE(400910, "拒绝执行该请求"),
    CONDITION_EXECUTE_ERROR(400911, "执行条件不符合")
    ;

    @Getter
    private final int code;
    @Getter
    private final String desc;


    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
