package com.azki.reservation.api.v1;

import com.azki.reservation.api.v1.model.ApiResponse;
import com.azki.reservation.api.v1.request.LoginOrSignupRequest;
import com.azki.reservation.api.v1.request.LoginWithPasswordRequest;
import com.azki.reservation.api.v1.request.OtpRequest;
import com.azki.reservation.api.v1.response.LoginOrSignupResponse;
import com.azki.reservation.constant.ApiStatus;
import com.azki.reservation.exception.auth.RateLimitException;
import com.azki.reservation.service.AuthenticationService;
import com.azki.reservation.util.RateLimitUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//TODO we could add a flow for login with password too
//TODO circuit breaker
//TODO bulkhead

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authService;
    private final RateLimitUtil rateLimitUtil;

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

    @PostMapping
    public ResponseEntity<ApiResponse<LoginOrSignupResponse>> loginWithPassword(@RequestBody @Valid LoginWithPasswordRequest request) {
        if (!rateLimitUtil.isLoginRequestAllowed(request.getMail())) {
            throw new RateLimitException();
        }
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.SUCCESS.name(), authService.loginWithPassword(request)));
    }
}
