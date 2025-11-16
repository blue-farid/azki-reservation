package com.azki.reservation.exception.customer;

import com.azki.reservation.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class CustomerNotFoundException extends BaseException {
    public static final String DESC = "exception.customer.not-found";

    public CustomerNotFoundException() {
        super(DESC);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getDescKey() {
        return DESC;
    }
}
