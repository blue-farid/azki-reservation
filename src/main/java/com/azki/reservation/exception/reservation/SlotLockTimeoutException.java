package com.azki.reservation.exception.reservation;

import com.azki.reservation.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class SlotLockTimeoutException extends BaseException {
    public static final String DESC = "exception.slot.lock-timeout";

    public SlotLockTimeoutException() {
        super(DESC);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.TOO_MANY_REQUESTS;
    }

    @Override
    public String getDescKey() {
        return DESC;
    }
}
