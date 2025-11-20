package com.azki.reservation.api.v1;

import com.azki.reservation.api.v1.model.ApiResponse;
import com.azki.reservation.api.v1.response.ReserveResponse;
import com.azki.reservation.constant.ApiStatus;
import com.azki.reservation.service.ReservationService;
import com.azki.reservation.util.SecurityUtil;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.azki.reservation.constant.AuthoritiesConstant.CUSTOMER;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Slf4j
public class ReservationController {
    private final ReservationService reservationService;
    private final SecurityUtil securityUtil;

    @Secured(CUSTOMER)
    @PostMapping
    @Bulkhead(name = "reserveBulkhead", type = Bulkhead.Type.SEMAPHORE)
    @Timed(value = "reservation.api.time", description = "Time taken by reservation endpoint")
    public ResponseEntity<ApiResponse<ReserveResponse>> reserve() {
        long start = System.nanoTime();
        try {
            return ResponseEntity.ok(new ApiResponse<>(ApiStatus.SUCCESS.name(),
                    reservationService.reserve(securityUtil.getCurrentUserId())));
        } finally {
            // this is just for the test!
            long end = System.nanoTime();
            log.info("Server-side execution time: {} ms", (end - start) / 1_000_000);
        }
    }

    @Secured(CUSTOMER)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancel(@PathVariable Long id) {
        reservationService.cancel(securityUtil.getCurrentUserId(), id);
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.SUCCESS.name()));
    }
}
