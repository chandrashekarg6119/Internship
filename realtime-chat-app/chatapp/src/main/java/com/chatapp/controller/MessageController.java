package com.chatapp.controller;

import com.chatapp.dto.MessageDTO;
import com.chatapp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/private")
    public ResponseEntity<MessageDTO.MessageResponse> sendPrivateMessage(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody MessageDTO.SendMessageRequest request) {
        return ResponseEntity.ok(messageService.sendPrivateMessage(userDetails.getUsername(), request));
    }

    @PostMapping("/group")
    public ResponseEntity<MessageDTO.MessageResponse> sendGroupMessage(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody MessageDTO.SendMessageRequest request) {
        return ResponseEntity.ok(messageService.sendGroupMessage(userDetails.getUsername(), request));
    }

    @GetMapping("/private/{userId}")
    public ResponseEntity<List<MessageDTO.MessageResponse>> getPrivateMessages(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getPrivateMessages(userDetails.getUsername(), userId));
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<MessageDTO.MessageResponse>> getGroupMessages(
            @PathVariable Long groupId) {
        return ResponseEntity.ok(messageService.getGroupMessages(groupId));
    }

    @PutMapping("/read/{senderId}")
    public ResponseEntity<?> markMessagesAsRead(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long senderId) {
        messageService.markMessagesAsRead(userDetails.getUsername(), senderId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessage(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long messageId) {
        messageService.deleteMessage(messageId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
