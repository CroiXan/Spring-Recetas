package com.example.users.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.users.Jwt.CustomerUserDetailsService;
import com.example.users.dao.UserDao;
import com.example.users.models.User;

public class CustomerUserDetailsServiceTest {

    @InjectMocks
    private CustomerUserDetailsService customerUserDetailsService;

    @Mock
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        String username = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(username);
        mockUser.setPassword("password123");

        when(userDao.findByEmailId(username)).thenReturn(mockUser);

        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());

        verify(userDao, times(1)).findByEmailId(username);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        String username = "nonexistent@example.com";

        when(userDao.findByEmailId(username)).thenReturn(null);

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            customerUserDetailsService.loadUserByUsername(username);
        });

        assertEquals("User not found with username: " + username, exception.getMessage());

        verify(userDao, times(1)).findByEmailId(username);
    }

    @Test
    void testGetUserDetail() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");

        when(userDao.findByEmailId("test@example.com")).thenReturn(mockUser);

        customerUserDetailsService.loadUserByUsername("test@example.com");
        User userDetail = customerUserDetailsService.getUserDetail();

        assertNotNull(userDetail);
        assertEquals("test@example.com", userDetail.getEmail());
        assertEquals("password123", userDetail.getPassword());

        verify(userDao, times(1)).findByEmailId("test@example.com");
    }
}
