package com.azki.reservation.api.v1;

import com.azki.reservation.api.v1.model.ApiResponse;
import com.azki.reservation.api.v1.request.LoginOrSignupRequest;
import com.azki.reservation.api.v1.request.LoginWithPasswordRequest;
import com.azki.reservation.api.v1.request.OtpRequest;
import com.azki.reservation.api.v1.request.SetPasswordRequest;
import com.azki.reservation.api.v1.response.LoginOrSignupResponse;
import com.azki.reservation.constant.ApiStatus;
import com.azki.reservation.exception.auth.RateLimitException;
import com.azki.reservation.service.AuthenticationService;
import com.azki.reservation.util.RateLimitUtil;
import com.azki.reservation.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.azki.reservation.constant.AuthoritiesConstant.CUSTOMER;

//TODO circuit breaker

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authService;
    private final RateLimitUtil rateLimitUtil;
    private final SecurityUtil securityUtil;

    @GetMapping("/otp")
    public ResponseEntity<ApiResponse<Void>> getOtp(@Valid OtpRequest request) {
        if (!rateLimitUtil.isOtpRequestAllowed(request.getMail())) {
            throw new RateLimitException();
        }

        authService.sendOtp(request);
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.SUCCESS.name()));
    }

    @PostMapping("/otp")
    public ResponseEntity<ApiResponse<LoginOrSignupResponse>> login(@RequestBody @Valid LoginOrSignupRequest request) {
        if (!rateLimitUtil.isLoginRequestAllowed(request.getMail())) {
            throw new RateLimitException();
        }
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.SUCCESS.name(), authService.login(request)));
    }

    @PostMapping("/passwords")
    @Secured(CUSTOMER)
    public ResponseEntity<ApiResponse<Void>> setPassword(@RequestBody @Valid SetPasswordRequest request) {
        authService.setPassword(request.setId(securityUtil.getCurrentUserId()));
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.SUCCESS.name()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LoginOrSignupResponse>> loginWithPassword(@RequestBody @Valid LoginWithPasswordRequest request) {
        if (!rateLimitUtil.isLoginRequestAllowed(request.getMail())) {
            throw new RateLimitException();
        }
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.SUCCESS.name(), authService.loginWithPassword(request)));
    }
}
