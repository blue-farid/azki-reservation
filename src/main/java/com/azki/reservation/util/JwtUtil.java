package com.azki.reservation.util;

import com.azki.reservation.config.properties.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final SecurityProperties properties;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(properties.getJwt().getSecret().getBytes());
    }

    public String generateCustomerToken(Long customerId, String mail, String... roles) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + properties.getJwt().getExpiration());

        Map<String, Object> claims = Map.of(
                "customerId", customerId,
                "roles", roles
        );

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(mail)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractMail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public List<String> extractRoles(String token) {
        return (List<String>) extractAllClaims(token).get("roles");
    }

    public Long extractCustomerId(String token) {
        Object id = extractAllClaims(token).get("customerId");
        return id != null ? Long.valueOf(id.toString()) : null;
    }
}
