package com.chatapp.websocket;

import com.chatapp.dto.MessageDTO;
import com.chatapp.entity.User;
import com.chatapp.service.MessageService;
import com.chatapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatWebSocketController {

    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private MessageService messageService;
    @Autowired private UserService userService;

    /**
     * Handle private chat messages via WebSocket
     * Client sends to: /app/chat.private
     */
    @MessageMapping("/chat.private")
    public void handlePrivateMessage(@Payload MessageDTO.SendMessageRequest request,
                                     Principal principal) {
        messageService.sendPrivateMessage(principal.getName(), request);
    }

    /**
     * Handle group chat messages via WebSocket
     * Client sends to: /app/chat.group
     */
    @MessageMapping("/chat.group")
    public void handleGroupMessage(@Payload MessageDTO.SendMessageRequest request,
                                   Principal principal) {
        messageService.sendGroupMessage(principal.getName(), request);
    }

    /**
     * Handle typing indicator
     * Client sends to: /app/chat.typing
     * Broadcasts to: /user/{receiver}/queue/typing  OR  /topic/group/{groupId}
     */
    @MessageMapping("/chat.typing")
    public void handleTyping(@Payload MessageDTO.WebSocketMessage message,
                              Principal principal) {
        User sender = userService.getCurrentUser(principal.getName());

        MessageDTO.WebSocketMessage typingEvent = MessageDTO.WebSocketMessage.builder()
                .eventType("TYPING")
                .senderId(sender.getId())
                .senderUsername(sender.getUsername())
                .typing(message.isTyping())
                .build();

        if (message.getReceiverId() != null) {
            // Private typing indicator
            User receiver = userService.getUserById(message.getReceiverId()) != null
                    ? userService.getCurrentUser(
                        userService.getUserById(message.getReceiverId()).getUsername()) : null;
            if (receiver != null) {
                messagingTemplate.convertAndSendToUser(
                        receiver.getUsername(), "/queue/typing", typingEvent);
            }
        } else if (message.getGroupId() != null) {
            // Group typing indicator
            typingEvent.setGroupId(message.getGroupId());
            messagingTemplate.convertAndSend("/topic/group/" + message.getGroupId() + "/typing", typingEvent);
        }
    }

    /**
     * Handle user joining/leaving (presence)
     * Client sends to: /app/chat.presence
     */
    @MessageMapping("/chat.presence")
    public void handlePresence(@Payload MessageDTO.WebSocketMessage message,
                                Principal principal) {
        User user = userService.getCurrentUser(principal.getName());

        MessageDTO.WebSocketMessage presenceEvent = MessageDTO.WebSocketMessage.builder()
                .eventType("STATUS_CHANGE")
                .senderId(user.getId())
                .senderUsername(user.getUsername())
                .statusChange(message.getStatusChange())
                .build();

        // Broadcast presence to all connected users
        messagingTemplate.convertAndSend("/topic/presence", presenceEvent);
    }
}
