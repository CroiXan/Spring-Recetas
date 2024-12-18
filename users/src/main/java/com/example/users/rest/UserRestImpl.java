package com.example.users.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.users.constants.Constant;
import com.example.users.service.UserService;
import com.example.users.utils.Util;
import com.example.users.wrapper.UserWrapper;

@RestController
public class UserRestImpl implements UserRest {

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {

        try {
            return userService.signUp(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Util.getResponseEntity(Constant.SOMETHING_WENT_WRONG, HttpStatus.UNPROCESSABLE_ENTITY);

    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
            return userService.login(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Util.getResponseEntity(Constant.SOMETHING_WENT_WRONG, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            return userService.getAllUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    // @Override
    // public ResponseEntity<String> updateUser(Map<String, String> requestMap) {
        
    //     try {
    //         return userService.update(requestMap);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return Util.getResponseEntity(Constant.SOMETHING_WENT_WRONG, HttpStatus.UNPROCESSABLE_ENTITY);
    // }

    @Override
    public ResponseEntity<String> checkToken() {
        try {
            return userService.checkToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Util.getResponseEntity(Constant.SOMETHING_WENT_WRONG, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try{
            return userService.changePassword(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Util.getResponseEntity(Constant.SOMETHING_WENT_WRONG, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    // @Override
    // public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
    //     try {
    //         return userService.forgotPassword(requestMap);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }    

    //     return Util.getResponseEntity(Constant.SOMETHING_WENT_WRONG, HttpStatus.UNPROCESSABLE_ENTITY);
    
    // }
}
