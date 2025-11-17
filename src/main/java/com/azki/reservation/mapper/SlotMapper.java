package com.azki.reservation.mapper;

import com.azki.reservation.api.v1.model.SlotDto;
import com.azki.reservation.domain.Slot;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SlotMapper {
    SlotDto toDto(Slot slot);

    Slot toEntity(SlotDto slot);
}
