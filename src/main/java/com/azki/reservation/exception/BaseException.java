package com.azki.reservation.exception;

import org.springframework.http.HttpStatusCode;

public abstract class BaseException extends RuntimeException {
    public BaseException(String message) {
        super(message);
    }

    public abstract HttpStatusCode getStatusCode();
    public abstract String getDescKey();
}
