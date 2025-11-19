package com.azki.reservation.api.v1.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SetPasswordRequest {
    private Long id;
    @NotBlank(message = "validation.password.blank")
    private String password;
    @NotBlank(message = "validation.confirm-password.blank")
    private String confirmPassword;
}
