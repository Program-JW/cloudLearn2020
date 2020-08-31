package com.ruigu.rbox.workflow.exceptions;

import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 全局异常表
 * @author lijiajia
 * @date 2019/4/24 11:48
 */
public class GlobalRuntimeException extends RuntimeException {

    @Setter
    @Getter
    private String msg;
    @Setter
    @Getter
    private int code;

    public GlobalRuntimeException(Throwable a, int code, String message) {
        super(a);
        this.code = code;
        this.msg = message;
    }

    public GlobalRuntimeException(int code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }

    public GlobalRuntimeException(ResponseCode responseCode) {
        super("code =" + responseCode.getCode() + "message = " + responseCode.getDesc());
        this.code = responseCode.getCode();
        this.msg = responseCode.getDesc();
    }
}
