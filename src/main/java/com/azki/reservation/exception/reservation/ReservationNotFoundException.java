package com.azki.reservation.exception.reservation;

import com.azki.reservation.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ReservationNotFoundException extends BaseException {
    public static final String DESC = "exception.reservation.not-found";

    public ReservationNotFoundException() {
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
