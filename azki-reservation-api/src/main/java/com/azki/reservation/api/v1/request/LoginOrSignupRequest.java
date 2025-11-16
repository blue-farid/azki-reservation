package com.azki.reservation.api.v1.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginOrSignupRequest {
    @NotBlank(message = "validation.mail.blank")
    private String mail;
    @NotBlank(message = "validation.otp.blank")
    private String otp;
}
