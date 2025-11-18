package com.azki.reservation.api.v1.response;

import com.azki.reservation.api.v1.model.SlotDto;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ReserveResponse {
    private Long id;
    private SlotDto slotDto;
}
