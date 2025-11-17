package com.azki.reservation.service.impl;

import com.azki.reservation.api.v1.response.ReserveResponse;
import com.azki.reservation.domain.Reservation;
import com.azki.reservation.domain.Slot;
import com.azki.reservation.exception.reservation.NoSlotAvailableException;
import com.azki.reservation.exception.reservation.ReservationAccessDeniedException;
import com.azki.reservation.exception.reservation.ReservationNotFoundException;
import com.azki.reservation.exception.reservation.SlotNotFoundException;
import com.azki.reservation.mapper.SlotMapper;
import com.azki.reservation.repository.ReservationRepository;
import com.azki.reservation.repository.SlotRepository;
import com.azki.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final SlotRepository slotRepository;
    private final ReservationRepository reservationRepository;
    private final SlotMapper slotMapper;

    @Override
    @Transactional
    public ReserveResponse reserve(Long customerId) {
        Slot avaliableSlot = slotRepository.findNextAvailableSlotForUpdate()
                .orElseThrow(NoSlotAvailableException::new);


        Reservation reservation = reservationRepository.save(new Reservation()
                .setCustomerId(customerId)
                .setSlotId(avaliableSlot.getId()));

        slotRepository.save(avaliableSlot.setReserved(true));

        return new ReserveResponse()
                .setId(reservation.getId())
                .setSlotDto(slotMapper.toDto(avaliableSlot));
    }

    @Override
    public void cancel(Long customerId, Long reserveId) {
        Reservation reservation = reservationRepository.findByIdAndActiveTrue(reserveId)
                .orElseThrow(ReservationNotFoundException::new);

        if (!reservation.getCustomerId().equals(customerId)) {
            throw new ReservationAccessDeniedException();
        }

        slotRepository.save(slotRepository.findById(reservation.getSlotId())
                .orElseThrow(SlotNotFoundException::new).setReserved(false));

        reservationRepository.save(reservation.setActive(false));
    }
}
