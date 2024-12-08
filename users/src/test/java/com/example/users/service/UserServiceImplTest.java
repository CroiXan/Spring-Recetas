package com.example.users.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.users.Jwt.CustomerUserDetailsService;
import com.example.users.Jwt.JwtFilter;
import com.example.users.Jwt.JwtUtil;
import com.example.users.constants.Constant;
import com.example.users.dao.UserDao;
import com.example.users.models.User;
import com.example.users.wrapper.UserWrapper;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserDao userDao;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomerUserDetailsService customerUserDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtFilter jwtFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signUp_ShouldReturnSuccessResponse() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name", "Test User");
        requestMap.put("email", "test@example.com");
        requestMap.put("password", "password");
        requestMap.put("phone", "123456789");

        when(userDao.findByEmailId("test@example.com")).thenReturn(null);

        ResponseEntity<String> response = userService.signUp(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Constant.SUCESS_REGISTER, response.getBody());
        verify(userDao, times(1)).save(any(User.class));
    }

    @Test
    void signUp_ShouldReturnEmailAlreadyExistsResponse() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name", "Test User");
        requestMap.put("email", "test@example.com");
        requestMap.put("password", "password");
        requestMap.put("phone", "123456789");

        when(userDao.findByEmailId("test@example.com")).thenReturn(new User());

        ResponseEntity<String> response = userService.signUp(requestMap);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Constant.EMAIL_ALREADY_EXISTS, response.getBody());
    }

    @Test
void login_ShouldReturnToken() {
    
    Map<String, String> requestMap = new HashMap<>();
    requestMap.put("email", "test@example.com");
    requestMap.put("password", "password");

    Authentication authentication = mock(Authentication.class);
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(true);

    User mockedUser = mock(User.class);
    when(mockedUser.getEmail()).thenReturn("test@example.com");
    when(mockedUser.getRole()).thenReturn("USER");
    when(mockedUser.getStatus()).thenReturn("true");

    when(customerUserDetailsService.getUserDetail()).thenReturn(mockedUser);

    when(jwtUtil.generateToken("test@example.com", "USER")).thenReturn("mockToken");

    ResponseEntity<String> response = userService.login(requestMap);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("{\"token\":\"mockToken\"}", response.getBody());
}

    @Test
    void login_ShouldReturnInvalidEmailOrPasswordResponse() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("email", "test@example.com");
        requestMap.put("password", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(RuntimeException.class);

        ResponseEntity<String> response = userService.login(requestMap);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"Invalid email or password\"}", response.getBody());
    }

    @Test
    void getAllUser_ShouldReturnListOfUsersWhenAdmin() {
        List<UserWrapper> users = Arrays.asList(new UserWrapper(), new UserWrapper());
        when(jwtFilter.isAdmin()).thenReturn(true);
        when(userDao.getAllUser()).thenReturn(users);

        ResponseEntity<List<UserWrapper>> response = userService.getAllUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void getAllUser_ShouldReturnUnauthorizedResponseWhenNotAdmin() {
        when(jwtFilter.isAdmin()).thenReturn(false);

        ResponseEntity<List<UserWrapper>> response = userService.getAllUser();

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    @Test
    void changePassword_ShouldReturnSuccessResponse() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("oldPassword", "oldPassword");
        requestMap.put("newPassword", "newPassword");

        User user = new User();
        user.setPassword("encodedOldPassword");
        when(jwtFilter.getCurrentUser()).thenReturn("test@example.com");
        when(userDao.findByEmailId("test@example.com")).thenReturn(user);
        when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(true);

        ResponseEntity<String> response = userService.changePassword(requestMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password changed successfully", response.getBody());
        verify(userDao, times(1)).save(user);
    }

    @Test
    void changePassword_ShouldReturnIncorrectPasswordResponse() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("oldPassword", "wrongPassword");
        requestMap.put("newPassword", "newPassword");

        User user = new User();
        user.setPassword("encodedOldPassword");
        when(jwtFilter.getCurrentUser()).thenReturn("test@example.com");
        when(userDao.findByEmailId("test@example.com")).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", "encodedOldPassword")).thenReturn(false);

        ResponseEntity<String> response = userService.changePassword(requestMap);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Constant.INCORRECT_PASSWORD, response.getBody());
    }
    
}
