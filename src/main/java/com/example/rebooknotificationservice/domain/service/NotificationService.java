package com.example.rebooknotificationservice.domain.service;

import com.example.rebooknotificationservice.common.PageResponse;
import com.example.rebooknotificationservice.domain.model.dto.NotificationResponse;
import com.example.rebooknotificationservice.domain.model.entity.Notification;
import com.example.rebooknotificationservice.domain.model.message.NotificationBookMessage;
import com.example.rebooknotificationservice.domain.model.message.NotificationChatMessage;
import com.example.rebooknotificationservice.domain.model.message.NotificationTradeMessage;
import com.example.rebooknotificationservice.domain.repository.NotificationRepository;
import com.example.rebooknotificationservice.exception.CMissingDataException;
import com.example.rebooknotificationservice.clientfeign.user.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationReader notificationReader;
    private final NotificationSettingService notificationSettingService;
    private final UserClient userClient;

    //알림생성
    @Transactional
    public void createBookNotification(NotificationBookMessage message, String userId) throws CMissingDataException {
        Notification notification = new Notification(message, userId);
        notificationRepository.save(notification);
    }

    @Transactional
    public void createChatNotification(NotificationChatMessage message) throws CMissingDataException {
        Notification notification = new Notification(message);
        notificationRepository.save(notification);
    }

    @Transactional
    public void createTradeNotification(NotificationTradeMessage message, String userId) throws CMissingDataException {
        Notification notification = new Notification(message, userId);
        notificationRepository.save(notification);
    }

    public PageResponse<NotificationResponse> getNotifications(String userId, Pageable pageable) {
        Page<Notification> notifications = notificationReader.getNotifications(userId, pageable);
        Page<NotificationResponse> responses = notifications.map(NotificationResponse::new);
        return new PageResponse<>(responses);
    }

    //알림읽음
    @Transactional
    public void readNotification(Long notificationId) {
        Notification notification = notificationReader.findById(notificationId);
        notification.setRead(true);
    }

    public Long getNotReadNumbers(String userId) {
        return notificationReader.getNotReadNumbers(userId);
    }

}
