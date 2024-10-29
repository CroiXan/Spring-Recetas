package com.example.users.Test;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TestController {
    
    @PostMapping(value = "test")
    public String welcome()
    {
        return "Welcome from secure endpoint";
    }
}
