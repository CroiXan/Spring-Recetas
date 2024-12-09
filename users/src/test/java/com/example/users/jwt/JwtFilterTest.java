package com.example.users.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.users.Jwt.CustomerUserDetailsService;
import com.example.users.Jwt.JwtFilter;
import com.example.users.Jwt.JwtUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilterTest {
    @InjectMocks
    private JwtFilter jwtFilter;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomerUserDetailsService service;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternal_ExcludedPath() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/user/login");

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        Claims claimsMock = mock(Claims.class);

        when(claimsMock.get("role")).thenReturn("USER");
        when(request.getServletPath()).thenReturn("/some/other/path");
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");

        UserDetails userDetails = new User("testUser", "password", new ArrayList<>());
        when(jwtUtil.extractUsername("valid-token")).thenReturn("testUser");
        when(jwtUtil.extractAllClaims("valid-token")).thenReturn(claimsMock);
        when(jwtUtil.validateToken("valid-token", userDetails)).thenReturn(true);
        when(service.loadUserByUsername("testUser")).thenReturn(userDetails);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("testUser",
                ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/some/other/path");
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");

        when(jwtUtil.extractUsername("invalid-token")).thenThrow(new RuntimeException("Invalid token"));

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testIsAdmin() {
        Claims claimsMock = mock(Claims.class);
        when(claimsMock.get("role")).thenReturn("ADMIN");

        jwtFilter.claims = claimsMock;

        assertTrue(jwtFilter.isAdmin());
    }

    @Test
    void testIsUser() {
        Claims claimsMock = mock(Claims.class);
        when(claimsMock.get("role")).thenReturn("USER");

        jwtFilter.claims = claimsMock;

        assertTrue(jwtFilter.isUser());
    }

    @Test
    void testGetCurrentUser() {
        jwtFilter.userName = "currentUser";

        assertEquals("currentUser", jwtFilter.getCurrentUser());
    }
}
