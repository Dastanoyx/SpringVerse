package com.sm.springverse.exception;

public class AIServiceException extends RuntimeException {
    private final String errorCode;

    public AIServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
