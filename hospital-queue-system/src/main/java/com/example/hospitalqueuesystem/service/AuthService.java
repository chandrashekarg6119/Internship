package com.example.hospitalqueuesystem.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.hospitalqueuesystem.entity.User;
import com.example.hospitalqueuesystem.repository.UserRepository;
import com.example.hospitalqueuesystem.security.JwtUtil;
import com.example.hospitalqueuesystem.dto.LoginRequest;
import com.example.hospitalqueuesystem.dto.RegisterRequest;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ Register User
    public User register(RegisterRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // ⚠️ later use BCrypt
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        return userRepository.save(user);
    }

    // ✅ Login User
    public String login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getUsername());
    }

}