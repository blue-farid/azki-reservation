package com.azki.reservation.repository;

import com.azki.reservation.domain.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.Optional;

// the skip lock scenario is the best for this type of concurrency but this solution
// is highly coupled with the database engine.
// so //TODO we need to add another way too (just for the interview I Impl two solutions)
public interface SlotRepository extends JpaRepository<Slot, Long> {
    @Query(value = """
                SELECT *
                FROM available_slot
                WHERE reserved = false
                  AND start_time >= NOW()
                ORDER BY start_time ASC
                LIMIT 1
                FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    Optional<Slot> findNextAvailableSlotForUpdate();

    Optional<Slot> findFirstByReservedFalseAndStartTimeGreaterThanEqualOrderByStartTimeAsc(Date startTime);
}
