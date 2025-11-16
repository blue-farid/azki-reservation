package com.azki.reservation.api.v1.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ValidationErrorResponse extends ErrorResponse {
    private Map<String, String> fieldErrorsMap;

    public ValidationErrorResponse(String message, Map<String, String> fieldErrorsMap) {
        super(message);
        this.fieldErrorsMap = fieldErrorsMap;
    }
}
