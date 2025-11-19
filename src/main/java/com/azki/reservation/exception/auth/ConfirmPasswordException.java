package com.azki.reservation.exception.auth;

import com.azki.reservation.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ConfirmPasswordException extends BaseException {
    public static final String DESC = "exception.auth.confirm";

    public ConfirmPasswordException() {
        super(DESC);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getDescKey() {
        return DESC;
    }
}
