package com.selloum.core.Exception;

import com.selloum.core.code.ErrorCode;
import com.selloum.core.code.Status;


public class CustomException extends RuntimeException {

	private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Status getStatus() {
        return errorCode.getStatus();
    }
}
