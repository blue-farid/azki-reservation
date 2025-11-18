package com.azki.reservation.api.v1.model;

import lombok.Data;

import java.util.Date;

@Data
public class SlotDto {
    private Long id;
    private Date startTime;
    private Date endTime;
}
