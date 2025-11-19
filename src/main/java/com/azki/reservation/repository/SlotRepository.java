package com.azki.reservation.repository;

import com.azki.reservation.domain.Slot;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

// the skip lock scenario is the best for this type of concurrency but this solution
// is highly coupled with the database engine.
// so //TODO we need to add another way too (just for the interview I Impl two solutions)
public interface SlotRepository extends JpaRepository<Slot, Long> {
    @Query(value = """
                SELECT *
                FROM available_slot
                WHERE is_reserved = false
                  AND start_time >= NOW()
                ORDER BY start_time ASC
                LIMIT 1
                FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    Optional<Slot> findNextAvailableSlotForUpdate();

    List<Slot> findByReservedFalseAndStartTimeGreaterThanOrderByStartTimeAsc(Date startTime, Pageable pageable);

// Alternative solution (DB-agnostic approach):
// A scheduled job (configurable, midnight daily...) will preload a fixed number of future slots
// (e.g., 10,000 - configurable) from the database into Redis.
// All reservation requests will first read from Redis and attempt to reserve from the cached set.
// Since Redis operates as a single-threaded cluster, this approach avoids concurrency issues.
// At the end of the day, when the scheduled job runs again, it will clear expired/remaining keys
// and repopulate Redis with the next upcoming slots.
// Additionally, during each reservation, the system will check the Redis cache size for the slot keys.
// When the cache size drops below a configurable threshold, an asynchronous background job will
// automatically fetch and append more slots from the database to keep the cache filled up to the desired capacity (configurable).

}
