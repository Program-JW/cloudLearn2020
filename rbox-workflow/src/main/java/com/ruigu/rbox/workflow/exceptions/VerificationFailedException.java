package com.ruigu.rbox.workflow.exceptions;


import com.ruigu.rbox.workflow.model.enums.ResponseCode;

/**
 * @author alan.zhao
 */
public class VerificationFailedException extends GlobalRuntimeException {
    public VerificationFailedException(Throwable a, int code, String message) {
        super(a, code, message);
    }

    public VerificationFailedException(int code, String message) {
        super(code, message);
    }

    public VerificationFailedException(ResponseCode responseCode) {
        super(responseCode);
    }
}
