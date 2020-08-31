package com.ruigu.rbox.workflow.exceptions;


import com.ruigu.rbox.cloud.kanai.web.exception.GlobalRuntimeException;

/**
 * 逻辑校验异常
 *
 * @author xiangbohua
 * @date 2019-05-30
 */
public class LogicException extends GlobalRuntimeException {

    public LogicException(String message) {
        super(-1, message);
    }

    public LogicException(Integer code, String message) {
        super(code, message);
    }

    public LogicException(String message, Throwable source) {
        super(source, -1, message);
    }
}