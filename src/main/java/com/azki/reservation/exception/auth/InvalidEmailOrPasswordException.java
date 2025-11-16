package com.azki.reservation.exception.auth;

import com.azki.reservation.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class InvalidEmailOrPasswordException extends BaseException {
    public static final String DESC = "exception.auth.invalid-email-or-password";

    public InvalidEmailOrPasswordException() {
        super(DESC);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.FORBIDDEN;
    }

    @Override
    public String getDescKey() {
        return DESC;
    }
}
