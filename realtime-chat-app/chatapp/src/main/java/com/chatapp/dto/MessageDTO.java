package com.chatapp.dto;

import com.chatapp.entity.Message;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

public class MessageDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendMessageRequest {
        private String content;
        private Long receiverId;   // for private chat
        private Long groupId;      // for group chat
        private Message.MessageType type;
        private String mediaUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageResponse {
        private Long id;
        private Long senderId;
        private String senderUsername;
        private String senderAvatar;
        private Long receiverId;
        private Long groupId;
        private String content;
        private Message.MessageType type;
        private Message.MessageStatus status;
        private String mediaUrl;
        private LocalDateTime createdAt;
        private boolean deleted;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WebSocketMessage {
        private String eventType; // CHAT, TYPING, STATUS_CHANGE, READ_RECEIPT
        private MessageResponse message;
        private Long senderId;
        private String senderUsername;
        private Long receiverId;
        private Long groupId;
        private String statusChange; // ONLINE, OFFLINE, AWAY
        private boolean typing;
    }
}
