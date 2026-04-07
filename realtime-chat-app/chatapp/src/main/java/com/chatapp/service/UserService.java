package com.chatapp.service;

import com.chatapp.dto.UserDTO;
import com.chatapp.entity.User;
import com.chatapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;

    public User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    public UserDTO.UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        return mapToResponse(user);
    }

    public List<UserDTO.UserResponse> searchUsers(String query) {
        return userRepository.searchUsers(query).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<UserDTO.UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<UserDTO.UserResponse> getOnlineUsers() {
        return userRepository.findOnlineUsers().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO.UserResponse updateProfile(String username, UserDTO.UpdateProfileRequest request) {
        User user = getCurrentUser(username);
        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getAvatar() != null) user.setAvatar(request.getAvatar());
        return mapToResponse(userRepository.save(user));
    }

    @Transactional
    public void updateUserStatus(String username, User.UserStatus status) {
        User user = getCurrentUser(username);
        user.setStatus(status);
        user.setLastSeen(LocalDateTime.now());
        userRepository.save(user);
    }

    public UserDTO.UserResponse mapToResponse(User user) {
        return UserDTO.UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatar(user.getAvatar())
                .bio(user.getBio())
                .status(user.getStatus())
                .lastSeen(user.getLastSeen())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
