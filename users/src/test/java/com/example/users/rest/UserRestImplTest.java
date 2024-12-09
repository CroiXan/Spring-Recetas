package com.example.users.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.users.constants.Constant;
import com.example.users.service.UserService;
import com.example.users.wrapper.UserWrapper;

public class UserRestImplTest {
    @InjectMocks
    private UserRestImpl userRestImpl;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignUp_Success() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("email", "test@example.com");
        requestMap.put("password", "password123");

        when(userService.signUp(requestMap))
                .thenReturn(new ResponseEntity<>("User registered successfully", HttpStatus.OK));

        ResponseEntity<String> response = userRestImpl.signUp(requestMap);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());

        verify(userService, times(1)).signUp(requestMap);
    }

    @Test
    void testSignUp_Exception() {
        Map<String, String> requestMap = new HashMap<>();
        when(userService.signUp(requestMap)).thenThrow(new RuntimeException("Error occurred"));

        ResponseEntity<String> response = userRestImpl.signUp(requestMap);

        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals(Constant.SOMETHING_WENT_WRONG, response.getBody());
    }

    @Test
    void testLogin_Success() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("email", "test@example.com");
        requestMap.put("password", "password123");

        when(userService.login(requestMap)).thenReturn(new ResponseEntity<>("Login successful", HttpStatus.OK));

        ResponseEntity<String> response = userRestImpl.login(requestMap);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful", response.getBody());

        verify(userService, times(1)).login(requestMap);
    }

    @Test
    void testLogin_Exception() {
        Map<String, String> requestMap = new HashMap<>();
        when(userService.login(requestMap)).thenThrow(new RuntimeException("Error occurred"));

        ResponseEntity<String> response = userRestImpl.login(requestMap);

        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals(Constant.SOMETHING_WENT_WRONG, response.getBody());
    }

    @Test
    void testGetAllUser_Success() {
        List<UserWrapper> mockUsers = Arrays.asList(new UserWrapper(null, "test@example.com", "USER", null, null));
        when(userService.getAllUser()).thenReturn(new ResponseEntity<>(mockUsers, HttpStatus.OK));

        ResponseEntity<List<UserWrapper>> response = userRestImpl.getAllUser();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUsers, response.getBody());

        verify(userService, times(1)).getAllUser();
    }

    @Test
    void testGetAllUser_Exception() {
        when(userService.getAllUser()).thenThrow(new RuntimeException("Error occurred"));

        ResponseEntity<List<UserWrapper>> response = userRestImpl.getAllUser();

        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testCheckToken_Success() {
        when(userService.checkToken()).thenReturn(new ResponseEntity<>("Token valid", HttpStatus.OK));

        ResponseEntity<String> response = userRestImpl.checkToken();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Token valid", response.getBody());

        verify(userService, times(1)).checkToken();
    }

    @Test
    void testCheckToken_Exception() {
        when(userService.checkToken()).thenThrow(new RuntimeException("Error occurred"));

        ResponseEntity<String> response = userRestImpl.checkToken();

        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals(Constant.SOMETHING_WENT_WRONG, response.getBody());
    }

    @Test
    void testChangePassword_Success() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("email", "test@example.com");
        requestMap.put("newPassword", "newPassword123");

        when(userService.changePassword(requestMap))
                .thenReturn(new ResponseEntity<>("Password changed successfully", HttpStatus.OK));

        ResponseEntity<String> response = userRestImpl.changePassword(requestMap);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password changed successfully", response.getBody());

        verify(userService, times(1)).changePassword(requestMap);
    }

    @Test
    void testChangePassword_Exception() {
        Map<String, String> requestMap = new HashMap<>();
        when(userService.changePassword(requestMap)).thenThrow(new RuntimeException("Error occurred"));

        ResponseEntity<String> response = userRestImpl.changePassword(requestMap);

        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals(Constant.SOMETHING_WENT_WRONG, response.getBody());
    }
}
