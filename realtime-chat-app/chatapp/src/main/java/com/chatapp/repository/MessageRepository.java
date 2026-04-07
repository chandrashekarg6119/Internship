package com.chatapp.repository;

import com.chatapp.entity.Message;
import com.chatapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Private messages between two users
    @Query("SELECT m FROM Message m WHERE " +
           "((m.sender = :user1 AND m.receiver = :user2) OR " +
           "(m.sender = :user2 AND m.receiver = :user1)) " +
           "AND m.deleted = false ORDER BY m.createdAt ASC")
    List<Message> findPrivateMessages(User user1, User user2);

    @Query("SELECT m FROM Message m WHERE " +
           "((m.sender = :user1 AND m.receiver = :user2) OR " +
           "(m.sender = :user2 AND m.receiver = :user1)) " +
           "AND m.deleted = false ORDER BY m.createdAt DESC")
    Page<Message> findPrivateMessagesPaged(User user1, User user2, Pageable pageable);

    // Group messages
    @Query("SELECT m FROM Message m WHERE m.group.id = :groupId AND m.deleted = false ORDER BY m.createdAt ASC")
    List<Message> findGroupMessages(Long groupId);

    @Query("SELECT m FROM Message m WHERE m.group.id = :groupId AND m.deleted = false ORDER BY m.createdAt DESC")
    Page<Message> findGroupMessagesPaged(Long groupId, Pageable pageable);

    // Unread messages for a user
    @Query("SELECT m FROM Message m WHERE m.receiver = :user AND m.status != 'READ' AND m.deleted = false")
    List<Message> findUnreadMessages(User user);

    // Mark messages as read
    @Modifying
    @Query("UPDATE Message m SET m.status = 'READ' WHERE m.receiver = :receiver AND m.sender.id = :senderId AND m.status != 'READ'")
    void markMessagesAsRead(User receiver, Long senderId);

    // Last message for conversation list
    @Query("SELECT m FROM Message m WHERE " +
           "((m.sender.id = :userId AND m.receiver.id = :otherId) OR " +
           "(m.sender.id = :otherId AND m.receiver.id = :userId)) " +
           "AND m.deleted = false ORDER BY m.createdAt DESC")
    Page<Message> findLastPrivateMessage(Long userId, Long otherId, Pageable pageable);

    // Count unread from specific sender
    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiver.id = :receiverId AND m.sender.id = :senderId AND m.status != 'READ' AND m.deleted = false")
    long countUnreadFromSender(Long receiverId, Long senderId);
}
