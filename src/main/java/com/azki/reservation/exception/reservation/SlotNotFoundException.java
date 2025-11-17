package com.azki.reservation.exception.reservation;

import com.azki.reservation.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class SlotNotFoundException extends BaseException {
    public static final String DESC = "exception.slot.not-found";

    public SlotNotFoundException() {
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
