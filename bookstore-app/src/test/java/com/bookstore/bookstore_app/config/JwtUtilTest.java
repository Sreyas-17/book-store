package com.bookstore.bookstore_app.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "testSecretKey123456789012345678901234567890");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 3600000L); // 1 hour
    }

    @Test
    void generateToken_Success() {
        String email = "test@example.com";
        String token = jwtUtil.generateToken(email);

        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
    }

    @Test
    void getEmailFromToken_Success() {
        String email = "test@example.com";
        String token = jwtUtil.generateToken(email);

        String extractedEmail = jwtUtil.getEmailFromToken(token);

        assertEquals(email, extractedEmail);
    }

    @Test
    void getEmailFromToken_InvalidToken() {
        assertThrows(Exception.class, () -> {
            jwtUtil.getEmailFromToken("invalid.token.here");
        });
    }

    @Test
    void validateToken_Success() {
        String email = "test@example.com";
        String token = jwtUtil.generateToken(email);

        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    void validateToken_InvalidToken() {
        boolean isValid = jwtUtil.validateToken("invalid.token.here");

        assertFalse(isValid);
    }

    @Test
    void validateToken_ExpiredToken() {
        // Set a very short expiration time
        ReflectionTestUtils.setField(jwtUtil, "expiration", 1L);
        String email = "test@example.com";
        String token = jwtUtil.generateToken(email);

        // Wait for token to expire
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean isValid = jwtUtil.validateToken(token);

        assertFalse(isValid);
    }

    @Test
    void isTokenExpired_NotExpired() {
        String email = "test@example.com";
        String token = jwtUtil.generateToken(email);

        boolean isExpired = jwtUtil.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    void isTokenExpired_Expired() {
        // Set a very short expiration time
        ReflectionTestUtils.setField(jwtUtil, "expiration", 1L);
        String email = "test@example.com";
        String token = jwtUtil.generateToken(email);

        // Wait for token to expire
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean isExpired = jwtUtil.isTokenExpired(token);

        assertTrue(isExpired);
    }

    @Test
    void isTokenExpired_InvalidToken() {
        boolean isExpired = jwtUtil.isTokenExpired("invalid.token.here");

        assertTrue(isExpired);
    }
} 