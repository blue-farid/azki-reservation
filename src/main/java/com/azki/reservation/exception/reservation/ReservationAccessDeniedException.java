package com.azki.reservation.exception.reservation;

import com.azki.reservation.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ReservationAccessDeniedException extends BaseException {
    public static final String DESC = "exception.reservation.access-denied";

    public ReservationAccessDeniedException() {
        super(DESC);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    public String getDescKey() {
        return DESC;
    }
}
