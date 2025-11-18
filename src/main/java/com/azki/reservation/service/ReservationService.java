package com.azki.reservation.service;

import com.azki.reservation.api.v1.response.ReserveResponse;

public interface ReservationService {
    ReserveResponse reserve(Long customerId);
    ReserveResponse reserveWithRedisLock(Long customerId);
    void cancel(Long customerId, Long reserveId);
}
