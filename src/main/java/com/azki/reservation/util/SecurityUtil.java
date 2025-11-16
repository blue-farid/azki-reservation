package com.azki.reservation.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class SecurityUtil {
    private final SecureRandom random = new SecureRandom();

    // It's better to use Secure Random
    public String generateOtp() {
        return String.valueOf(random.nextInt(100_000, 999_999));
    }

    public Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
