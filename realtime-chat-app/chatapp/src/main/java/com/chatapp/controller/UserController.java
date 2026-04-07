package com.chatapp.controller;

import com.chatapp.dto.UserDTO;
import com.chatapp.entity.User;
import com.chatapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDTO.UserResponse> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getUserById(
                userService.getCurrentUser(userDetails.getUsername()).getId()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserDTO.UserResponse> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserDTO.UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(userDetails.getUsername(), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO.UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO.UserResponse>> searchUsers(@RequestParam String q) {
        return ResponseEntity.ok(userService.searchUsers(q));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO.UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/online")
    public ResponseEntity<List<UserDTO.UserResponse>> getOnlineUsers() {
        return ResponseEntity.ok(userService.getOnlineUsers());
    }

    @PostMapping("/status")
    public ResponseEntity<?> updateStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String status) {
        try {
            userService.updateUserStatus(userDetails.getUsername(), User.UserStatus.valueOf(status));
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status: " + status);
        }
    }
}
