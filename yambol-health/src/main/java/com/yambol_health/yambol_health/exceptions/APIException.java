package com.yambol_health.yambol_health.exceptions;

public class APIException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public APIException() {
    }

    public APIException(String message) {
        super(message);
    }
}
