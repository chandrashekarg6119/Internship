package com.example.hospitalqueuesystem.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*") // IMPORTANT for React
public class AuthController {

    // Temporary in-memory user store (no DB for now)
    private Map<String, String> users = new HashMap<>();

    // ✅ REGISTER
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> data) {

        String username = data.get("username");
        String password = data.get("password");

        Map<String, String> response = new HashMap<>();

        if (users.containsKey(username)) {
            response.put("message", "User already exists");
        } else {
            users.put(username, password);
            response.put("message", "User registered successfully");
        }

        return response;
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> data) {

        String username = data.get("username");
        String password = data.get("password");

        Map<String, String> response = new HashMap<>();

        // check user exists
        if (!users.containsKey(username)) {
            response.put("error", "User not found");
            return response;
        }

        // check password
        if (!users.get(username).equals(password)) {
            response.put("error", "Invalid password");
            return response;
        }

        // SUCCESS → send token
        response.put("token", "dummy-token-" + username);

        return response;
    }
}