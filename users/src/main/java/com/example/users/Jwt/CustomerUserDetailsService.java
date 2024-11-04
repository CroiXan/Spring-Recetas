package com.example.users.Jwt;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.users.dao.UserDao;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Slf4j
@Service
public class CustomerUserDetailsService implements UserDetailsService{
    

    @Autowired
    UserDao userDao;

    private com.example.users.models.User userDetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Username: " + username);
        userDetail = userDao.findByEmailId(username); 
        if(!Objects.isNull(userDetail)){
            return new org.springframework.security.core.userdetails.User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
        }else{
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    public com.example.users.models.User getUserDetail() {
        return userDetail;
    }
}
