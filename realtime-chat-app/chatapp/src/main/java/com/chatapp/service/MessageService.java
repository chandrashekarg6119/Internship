package com.chatapp.service;

import com.chatapp.dto.MessageDTO;
import com.chatapp.entity.Group;
import com.chatapp.entity.Message;
import com.chatapp.entity.User;
import com.chatapp.repository.GroupRepository;
import com.chatapp.repository.MessageRepository;
import com.chatapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired private MessageRepository messageRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private GroupRepository groupRepository;
    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private RedisTemplate<String, Object> redisTemplate;
    @Autowired private NotificationService notificationService;

    private static final String CACHE_PREFIX = "chat:messages:";
    private static final Duration CACHE_TTL = Duration.ofMinutes(30);

    @Transactional
    public MessageDTO.MessageResponse sendPrivateMessage(String senderUsername,
                                                          MessageDTO.SendMessageRequest request) {
        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(request.getContent())
                .type(request.getType() != null ? request.getType() : Message.MessageType.TEXT)
                .status(Message.MessageStatus.SENT)
                .mediaUrl(request.getMediaUrl())
                .build();

        message = messageRepository.save(message);
        MessageDTO.MessageResponse response = mapToResponse(message);

        // Deliver via WebSocket
        MessageDTO.WebSocketMessage wsMessage = MessageDTO.WebSocketMessage.builder()
                .eventType("CHAT")
                .message(response)
                .build();

        messagingTemplate.convertAndSendToUser(receiver.getUsername(), "/queue/messages", wsMessage);

        // Send notification
        notificationService.sendMessageNotification(sender, receiver, message);

        // Invalidate cache
        evictConversationCache(sender.getId(), receiver.getId());

        return response;
    }

    @Transactional
    public MessageDTO.MessageResponse sendGroupMessage(String senderUsername,
                                                        MessageDTO.SendMessageRequest request) {
        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (!group.getMembers().contains(sender)) {
            throw new RuntimeException("You are not a member of this group");
        }

        Message message = Message.builder()
                .sender(sender)
                .group(group)
                .content(request.getContent())
                .type(request.getType() != null ? request.getType() : Message.MessageType.TEXT)
                .status(Message.MessageStatus.SENT)
                .mediaUrl(request.getMediaUrl())
                .build();

        message = messageRepository.save(message);
        MessageDTO.MessageResponse response = mapToResponse(message);

        // Broadcast to group topic
        MessageDTO.WebSocketMessage wsMessage = MessageDTO.WebSocketMessage.builder()
                .eventType("CHAT")
                .message(response)
                .build();

        messagingTemplate.convertAndSend("/topic/group/" + group.getId(), wsMessage);

        // Evict group message cache
        redisTemplate.delete(CACHE_PREFIX + "group:" + group.getId());

        return response;
    }

    @SuppressWarnings("unchecked")
    public List<MessageDTO.MessageResponse> getPrivateMessages(String currentUsername, Long otherUserId) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String cacheKey = CACHE_PREFIX + "private:" + Math.min(currentUser.getId(), otherUserId)
                          + ":" + Math.max(currentUser.getId(), otherUserId);

        // Try cache first
        Object cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached instanceof List) {
            return (List<MessageDTO.MessageResponse>) cached;
        }

        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<MessageDTO.MessageResponse> messages = messageRepository
                .findPrivateMessages(currentUser, otherUser).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        // Cache result
        redisTemplate.opsForValue().set(cacheKey, messages, CACHE_TTL);

        return messages;
    }

    @SuppressWarnings("unchecked")
    public List<MessageDTO.MessageResponse> getGroupMessages(Long groupId) {
        String cacheKey = CACHE_PREFIX + "group:" + groupId;

        Object cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached instanceof List) {
            return (List<MessageDTO.MessageResponse>) cached;
        }

        List<MessageDTO.MessageResponse> messages = messageRepository
                .findGroupMessages(groupId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        redisTemplate.opsForValue().set(cacheKey, messages, CACHE_TTL);

        return messages;
    }

    @Transactional
    public void markMessagesAsRead(String currentUsername, Long senderId) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        messageRepository.markMessagesAsRead(currentUser, senderId);
        evictConversationCache(currentUser.getId(), senderId);

        // Notify sender that messages were read
        MessageDTO.WebSocketMessage wsMessage = MessageDTO.WebSocketMessage.builder()
                .eventType("READ_RECEIPT")
                .receiverId(currentUser.getId())
                .senderId(senderId)
                .build();
        User sender = userRepository.findById(senderId).orElse(null);
        if (sender != null) {
            messagingTemplate.convertAndSendToUser(sender.getUsername(), "/queue/read-receipt", wsMessage);
        }
    }

    @Transactional
    public void deleteMessage(Long messageId, String username) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!message.getSender().getUsername().equals(username)) {
            throw new RuntimeException("Not authorized to delete this message");
        }

        message.setDeleted(true);
        messageRepository.save(message);
    }

    private void evictConversationCache(Long id1, Long id2) {
        String cacheKey = CACHE_PREFIX + "private:" + Math.min(id1, id2) + ":" + Math.max(id1, id2);
        redisTemplate.delete(cacheKey);
    }

    public MessageDTO.MessageResponse mapToResponse(Message message) {
        return MessageDTO.MessageResponse.builder()
                .id(message.getId())
                .senderId(message.getSender().getId())
                .senderUsername(message.getSender().getUsername())
                .senderAvatar(message.getSender().getAvatar())
                .receiverId(message.getReceiver() != null ? message.getReceiver().getId() : null)
                .groupId(message.getGroup() != null ? message.getGroup().getId() : null)
                .content(message.getContent())
                .type(message.getType())
                .status(message.getStatus())
                .mediaUrl(message.getMediaUrl())
                .createdAt(message.getCreatedAt())
                .deleted(message.isDeleted())
                .build();
    }
}
