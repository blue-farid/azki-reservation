package com.azki.reservation.service.impl;

import com.azki.reservation.api.v1.response.ReserveResponse;
import com.azki.reservation.config.properties.RedissonProperties;
import com.azki.reservation.domain.Reservation;
import com.azki.reservation.domain.Slot;
import com.azki.reservation.exception.reservation.*;
import com.azki.reservation.mapper.SlotMapper;
import com.azki.reservation.repository.ReservationRepository;
import com.azki.reservation.repository.SlotRepository;
import com.azki.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final SlotRepository slotRepository;
    private final ReservationRepository reservationRepository;
    private final SlotMapper slotMapper;
    private final RedissonClient redissonClient;
    private final RedissonProperties properties;

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

    // consider the skip for lock on the database is the best solution.
    @Override
    @Transactional
    public ReserveResponse reserveWithRedisLock(Long customerId) {
        int batchSize = properties.getLock().getSlot().getMaxTry();
        List<Slot> slotBatch = slotRepository
                .findByReservedFalseAndStartTimeGreaterThanOrderByStartTimeAsc(
                        new Date(),
                        PageRequest.of(0, batchSize)
                );

        if (slotBatch.isEmpty()) {
            throw new NoSlotAvailableException();
        }

        for (Slot slot : slotBatch) {
            String lockKey = properties.getLock().getSlot().getKey() + slot.getId();
            RLock lock = redissonClient.getLock(lockKey);
            boolean acquired = false;
            try {
                acquired = lock.tryLock(
                        properties.getLock().getSlot().getWaitTime(),
                        properties.getLock().getSlot().getLeaseTime(),
                        TimeUnit.MILLISECONDS
                );

                if (!acquired) {
                    continue;
                }
                Slot freshSlot = slotRepository.findById(slot.getId())
                        .orElseThrow(NoSlotAvailableException::new);
                if (freshSlot.isReserved()) {
                    continue;
                }
                freshSlot.setReserved(true);
                slotRepository.save(freshSlot);

                Reservation reservation = reservationRepository.save(
                        new Reservation()
                                .setCustomerId(customerId)
                                .setSlotId(freshSlot.getId())
                );

                return new ReserveResponse()
                        .setId(reservation.getId())
                        .setSlotDto(slotMapper.toDto(freshSlot));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            } finally {
                if (acquired && lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
        throw new SlotLockTimeoutException();
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
