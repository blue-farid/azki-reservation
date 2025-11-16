package com.azki.reservation.api.v1.response;

import com.azki.reservation.api.v1.model.CustomerDto;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginOrSignupResponse {
    private CustomerDto customer;
    private String token;
}
