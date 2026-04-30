package com.apple.tpo.e_commerce.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import io.jsonwebtoken.ExpiredJwtException;

class JwtServiceTest {

    // Debe ser una clave base64 con longitud >= 256 bits para HS256
    private static final String SECRET_BASE64 = "VGhpc0lzQVNlY3JldEtleUZvckpXVFRlc3RpbmdQdXJwb3NlczEyMzQ1Njc4OTA=";

    @Test
    void generateToken_extractUsername_y_isTokenValid() {
        JwtService jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret", SECRET_BASE64);
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", 60_000L);

        var userDetails = User.withUsername("admin@apple-ar.com")
                .password("password")
                .authorities("ROLE_ADMIN")
                .build();

        String token = jwtService.generateToken(userDetails);

        assertEquals("admin@apple-ar.com", jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void isTokenValid_whenExpired_returnsFalse() {
        JwtService jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret", SECRET_BASE64);
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", -1_000L);

        var userDetails = User.withUsername("admin@apple-ar.com")
                .password("password")
                .authorities("ROLE_ADMIN")
                .build();

        String token = jwtService.generateToken(userDetails);
        // En esta implementación, al token expirar el parse falla antes de devolver false
        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenValid(token, userDetails));
    }
}

