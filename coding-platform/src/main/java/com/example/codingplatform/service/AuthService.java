package com.example.codingplatform.service;

import com.example.codingplatform.entity.User;
import com.example.codingplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    // REGISTER
    public User register(User user) {
        user.setRole("USER");
        return userRepository.save(user);
    }

    // LOGIN
    public String login(String username, String password) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            return "User not found";
        }

        if (!user.getPassword().equals(password)) {
            return "Invalid password";
        }

        return "Login successful";
    }
}