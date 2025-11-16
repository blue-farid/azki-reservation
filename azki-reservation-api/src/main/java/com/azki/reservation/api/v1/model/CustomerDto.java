package com.azki.reservation.api.v1.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CustomerDto {
    private Long id;
    @NotBlank(message = "validation.mail.blank")
    private String mail;
}
