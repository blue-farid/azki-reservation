package com.azki.reservation.mapper;

import com.azki.reservation.api.v1.model.CustomerDto;
import com.azki.reservation.domain.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDto entityToDto(Customer customer);
}
