package com.sm.springverse.exception;

public class ServiceProcessingException extends RuntimeException {
    public ServiceProcessingException(String message) {
        super(message);
    }
    public ServiceProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
