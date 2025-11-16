package com.azki.reservation.exception.handler;

import com.azki.reservation.api.v1.model.ApiResponse;
import com.azki.reservation.api.v1.model.ErrorResponse;
import com.azki.reservation.constant.ApiStatus;
import com.azki.reservation.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GeneralExceptionHandler {
    private final MessageSourceUtil messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> unhandledException(Exception ex) {
        log.error("Business exception", ex);
        return new ResponseEntity<>(new ApiResponse<>(ApiStatus.FAILURE.name(),
                new ErrorResponse(messageSource.getMessageIfExist("exception.internal-server-error"))), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
