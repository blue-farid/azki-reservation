package com.azki.reservation.exception.reservation;

import com.azki.reservation.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class NoSlotAvailableException extends BaseException {
    public static final String DESC = "exception.slot.not-available";

    public NoSlotAvailableException() {
        super(DESC);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public String getDescKey() {
        return DESC;
    }
}
