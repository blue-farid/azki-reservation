package com.azki.reservation.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "azki-reservation.security")
public class SecurityProperties {
    private Jwt jwt;
    private RateLimit rateLimit;
    private Integer otpExpiration;

    @Data
    public static class Jwt {
        private String secret;
        private Integer expiration;
    }

    @Data
    public static class RateLimit {
        private LimitConfig otp;
        private LimitConfig login;
    }

    @Data
    public static class LimitConfig {
        private Integer interval;
        private Integer limit;
        private String key;
    }
}
