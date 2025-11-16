package com.azki.reservation.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "azki-reservation.redisson")
public class RedissonProperties {
//    private Lock lock;
    private Bucket rateLimiter;

    @Data
    public static class LockConfig {
        private String key;
        private Integer waitTime;
        private Integer leaseTime;
    }

    @Data
    public static class Bucket {
        private String key;
    }
}
