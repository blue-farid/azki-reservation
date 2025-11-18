package com.azki.reservation.api.v1;

import com.azki.reservation.api.v1.model.ApiResponse;
import com.azki.reservation.api.v1.response.ReserveResponse;
import com.azki.reservation.constant.ApiStatus;
import com.azki.reservation.service.ReservationService;
import com.azki.reservation.util.SecurityUtil;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.azki.reservation.constant.AuthoritiesConstant.CUSTOMER;

//TODO change delete status from 200
//TODO add bulkhead here on reserve

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final SecurityUtil securityUtil;

    @Secured(CUSTOMER)
    @PostMapping
    @Bulkhead(name = "reserveBulkhead", type = Bulkhead.Type.SEMAPHORE)
    public ResponseEntity<ApiResponse<ReserveResponse>> reserve() {
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.SUCCESS.name(),
                reservationService.reserve(securityUtil.getCurrentUserId())));
    }

    @Secured(CUSTOMER)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancel(@PathVariable Long id) {
        reservationService.cancel(securityUtil.getCurrentUserId(), id);
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.SUCCESS.name()));
    }
}
