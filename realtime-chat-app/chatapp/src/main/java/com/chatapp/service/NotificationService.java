package com.chatapp.service;

import com.chatapp.entity.Message;
import com.chatapp.entity.Notification;
import com.chatapp.entity.User;
import com.chatapp.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    @Autowired private NotificationRepository notificationRepository;
    @Autowired private SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void sendMessageNotification(User sender, User receiver, Message message) {
        Notification notification = Notification.builder()
                .recipient(receiver)
                .sender(sender)
                .content(sender.getFullName() + " sent you a message: " +
                         truncate(message.getContent(), 50))
                .type(Notification.NotificationType.NEW_MESSAGE)
                .referenceId(message.getId())
                .build();

        notification = notificationRepository.save(notification);

        // Push notification via WebSocket
        messagingTemplate.convertAndSendToUser(
                receiver.getUsername(),
                "/queue/notifications",
                notification
        );
    }

    @Transactional
    public void sendGroupNotification(User sender, User recipient, Long groupId, String groupName) {
        Notification notification = Notification.builder()
                .recipient(recipient)
                .sender(sender)
                .content(sender.getFullName() + " sent a message in group: " + groupName)
                .type(Notification.NotificationType.GROUP_MESSAGE)
                .referenceId(groupId)
                .build();

        notification = notificationRepository.save(notification);

        messagingTemplate.convertAndSendToUser(
                recipient.getUsername(),
                "/queue/notifications",
                notification
        );
    }

    public List<Notification> getUserNotifications(User user) {
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(user);
    }

    public List<Notification> getUnreadNotifications(User user) {
        return notificationRepository.findByRecipientAndReadFalseOrderByCreatedAtDesc(user);
    }

    public long getUnreadCount(User user) {
        return notificationRepository.countByRecipientAndReadFalse(user);
    }

    @Transactional
    public void markAllAsRead(User user) {
        notificationRepository.markAllAsRead(user);
    }

    @Transactional
    public void markAsRead(Long notificationId, User user) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            if (n.getRecipient().getId().equals(user.getId())) {
                n.setRead(true);
                notificationRepository.save(n);
            }
        });
    }

    private String truncate(String text, int maxLength) {
        if (text == null) return "";
        return text.length() <= maxLength ? text : text.substring(0, maxLength) + "...";
    }
}
