package com.example.codingplatform.controller;

import com.example.codingplatform.entity.User;
import com.example.codingplatform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // REGISTER
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return authService.register(user);
    }

    // LOGIN
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return authService.login(user.getUsername(), user.getPassword());
    }
}