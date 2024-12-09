package com.example.users.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.users.Jwt.JwtUtil;

import io.jsonwebtoken.Claims;

public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void testExtractUsername() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username, "USER");

        String extractedUsername = jwtUtil.extractUsername(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    void testExtractExpiration() {
        String token = jwtUtil.generateToken("testUser", "USER");

        Date expiration = jwtUtil.extractExpiration(token);

        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void testExtractAllClaims() {
        String token = jwtUtil.generateToken("testUser", "USER");

        Claims claims = jwtUtil.extractAllClaims(token);

        assertNotNull(claims);
        assertEquals("USER", claims.get("role"));
        assertEquals("testUser", claims.getSubject());
    }

    @Test
    void testGenerateToken() {
        String username = "testUser";
        String role = "ADMIN";

        String token = jwtUtil.generateToken(username, role);

        assertNotNull(token);
        Claims claims = jwtUtil.extractAllClaims(token);
        assertEquals(role, claims.get("role"));
        assertEquals(username, claims.getSubject());
    }

    @Test
    void testValidateToken_ValidToken() {
        UserDetails userDetails = new User("testUser", "password", new ArrayList<>());
        String token = jwtUtil.generateToken("testUser", "USER");

        Boolean isValid = jwtUtil.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void testValidateToken_ExpiredToken() throws InterruptedException {
        JwtUtil mockJwtUtil = Mockito.spy(jwtUtil);
        Mockito.doReturn(new Date(System.currentTimeMillis() - 1000))
                .when(mockJwtUtil).extractExpiration(Mockito.anyString());

        UserDetails userDetails = new User("testUser", "password", new ArrayList<>());
        String token = jwtUtil.generateToken("testUser", "USER");

        Boolean isValid = mockJwtUtil.validateToken(token, userDetails);

        assertFalse(isValid);
    }
}
