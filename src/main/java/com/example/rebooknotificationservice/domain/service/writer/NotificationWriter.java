package com.example.rebooknotificationservice.domain.service.writer;

import com.example.rebooknotificationservice.domain.model.entity.Notification;
import com.example.rebooknotificationservice.external.rabbitmq.message.NotificationBookMessage;
import com.example.rebooknotificationservice.external.rabbitmq.message.NotificationChatMessage;
import com.example.rebooknotificationservice.external.rabbitmq.message.NotificationTradeMessage;
import com.example.rebooknotificationservice.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationWriter {
    private final NotificationRepository notificationRepository;

    public Notification createBookNotification(NotificationBookMessage message, String userId) {
        Notification notification = new Notification(message, userId);
        return notificationRepository.save(notification);
    }

    public Notification createChatNotification(NotificationChatMessage message) {
        Notification notification = new Notification(message);
        return notificationRepository.save(notification);
    }

    public Notification createTradeNotification(NotificationTradeMessage message, String userId) {
        Notification notification = new Notification(message, userId);
        return notificationRepository.save(notification);
    }

    public void markAsRead(Notification notification) {
        notification.setRead(true);
    }
}
