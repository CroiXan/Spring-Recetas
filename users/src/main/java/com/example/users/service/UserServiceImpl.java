package com.example.users.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.example.users.constants.Constant;
import com.example.users.dao.UserDao;
import com.example.users.Jwt.CustomerUserDetailsService;
import com.example.users.Jwt.JwtFilter;
import com.example.users.Jwt.JwtUtil;
import com.example.users.models.User;
import com.example.users.utils.Util;
import com.example.users.wrapper.UserWrapper;

import javax.xml.bind.DatatypeConverter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDao userDao;

    @Autowired
    CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtFilter jwtFilter;


    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signUp method of UserServiceImpl", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = userDao.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userDao.save(getUserFromMap(requestMap));
                    return Util.getResponseEntity(Constant.SUCESS_REGISTER, HttpStatus.OK);

                } else {
                    return Util.getResponseEntity(Constant.EMAIL_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
                }
            } else {
                log.error("Invalid request map");
                return Util.getResponseEntity(Constant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Util.getResponseEntity(Constant.SOMETHING_WENT_WRONG, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("name") && requestMap.containsKey("email") && requestMap.containsKey("password")
                && requestMap.containsKey("phone")) {
            return true;
        }
        return false;
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setPhone(requestMap.get("phone"));
        user.setEmail(requestMap.get("email"));
        // user.setPassword(requestMap.get("password"));
        user.setPassword(passwordEncoder.encode(requestMap.get("password")));
        user.setRole("USER");
        user.setStatus("false");
        return user;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("inside login");
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));
            if (auth.isAuthenticated()) {
                if (customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<String>("{\"token\":\"" +
                            jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
                                    customerUserDetailsService.getUserDetail().getRole())
                            + "\"}", HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("{\"message\":\"Wait for admin approval\"}",
                            HttpStatus.BAD_REQUEST);
                }
            }

        } catch (Exception e) {
            log.error(null, e);
        }

        return new ResponseEntity<String>("{\"message\":\"Invalid email or password\"}", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if (jwtFilter.isAdmin()) {
                return new ResponseEntity<>(userDao.getAllUser(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNPROCESSABLE_ENTITY);
    }


    @Override
    public ResponseEntity<String> checkToken() {
        
        return Util.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObj = userDao.findByEmailId(jwtFilter.getCurrentUser());
            if (userObj != null) {
                if (passwordEncoder.matches(requestMap.get("oldPassword"), userObj.getPassword())) {
                    userObj.setPassword(passwordEncoder.encode(requestMap.get("newPassword"))); 
                    userDao.save(userObj);
                    return Util.getResponseEntity("Password changed successfully", HttpStatus.OK);
                }
                return Util.getResponseEntity(Constant.INCORRECT_PASSWORD, HttpStatus.BAD_REQUEST);
            }
            return Util.getResponseEntity(Constant.SOMETHING_WENT_WRONG, HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            e.printStackTrace();
            return Util.getResponseEntity(Constant.SOMETHING_WENT_WRONG, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    
}
