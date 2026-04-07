package com.chatapp.websocket;

import com.chatapp.dto.MessageDTO;
import com.chatapp.entity.User;
import com.chatapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private UserService userService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = headerAccessor.getUser();

        if (user != null) {
            logger.info("User connected: {}", user.getName());
            try {
                userService.updateUserStatus(user.getName(), User.UserStatus.ONLINE);

                MessageDTO.WebSocketMessage presenceEvent = MessageDTO.WebSocketMessage.builder()
                        .eventType("STATUS_CHANGE")
                        .senderUsername(user.getName())
                        .statusChange("ONLINE")
                        .build();

                messagingTemplate.convertAndSend("/topic/presence", presenceEvent);
            } catch (Exception e) {
                logger.error("Error updating user status on connect: {}", e.getMessage());
            }
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = headerAccessor.getUser();

        if (user != null) {
            logger.info("User disconnected: {}", user.getName());
            try {
                userService.updateUserStatus(user.getName(), User.UserStatus.OFFLINE);

                MessageDTO.WebSocketMessage presenceEvent = MessageDTO.WebSocketMessage.builder()
                        .eventType("STATUS_CHANGE")
                        .senderUsername(user.getName())
                        .statusChange("OFFLINE")
                        .build();

                messagingTemplate.convertAndSend("/topic/presence", presenceEvent);
            } catch (Exception e) {
                logger.error("Error updating user status on disconnect: {}", e.getMessage());
            }
        }
    }
}
