package com.azki.reservation.api.v1.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OtpRequest {
    @NotBlank(message = "validation.mail.blank")
    private String mail;
}
