package com.example.hospitalqueuesystem.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.hospitalqueuesystem.entity.User;
import com.example.hospitalqueuesystem.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/register")
    public User register(@RequestBody User user){

        return userRepository.save(user);

    }

}