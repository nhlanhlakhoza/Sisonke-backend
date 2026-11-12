
package com.example.Sisonke.backend.service;

import com.example.Sisonke.backend.models.user.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    private final String SECRET_KEY =
        "4bb6d1dfbafb64a681139d1586b6f1160d18159afd57c8c79136d7490630407c";

    // Extract email from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract user ID from token
    public UUID extractUserId(String token) {
        String userId = extractClaim(
            token,
            claims -> claims.get("userId", String.class)
        );

        return UUID.fromString(userId);
    }

    // Validate token
    public boolean isValid(String token, UserDetails user) {
        try {
            String username = extractUsername(token);

            return username.equals(user.getUsername())
                && !isTokenExpired(token);

        } catch (Exception e) {
            return false;
        }
    }

    // Check expiration
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract claims
    public <T> T extractClaim(
        String token,
        Function<Claims, T> resolver
    ) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    // Parse token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    // Generate token
    public String generateToken(User user) {

        Date now = new Date();

        Date expiryDate = new Date(
            now.getTime() + 24 * 60 * 60 * 1000
        );

        return Jwts.builder()
            .subject(user.getEmail())
            .claim("userId", user.getId().toString())
            .claim("fullName", user.getFullName())
            .claim("email", user.getEmail())
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(getSigningKey())
            .compact();
    }

    // Signing key
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
            SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }
}
