package com.example.rebooknotificationservice.domain.service;

import com.example.rebooknotificationservice.common.enums.Type;
import com.example.rebooknotificationservice.domain.model.dto.NotificationResponse;
import com.example.rebooknotificationservice.domain.model.dto.NotificationSettingResponse;
import com.example.rebooknotificationservice.domain.model.entity.Notification;
import com.example.rebooknotificationservice.domain.model.entity.NotificationSetting;
import com.example.rebooknotificationservice.domain.service.reader.NotificationReader;
import com.example.rebooknotificationservice.domain.service.reader.NotificationSettingReader;
import com.example.rebooknotificationservice.domain.service.writer.NotificationWriter;
import com.example.rebooknotificationservice.domain.service.writer.NotificationSettingWriter;
import com.example.rebooknotificationservice.external.rabbitmq.message.NotificationBookMessage;
import com.example.rebooknotificationservice.external.rabbitmq.message.NotificationChatMessage;
import com.example.rebooknotificationservice.external.rabbitmq.message.NotificationTradeMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationReader notificationReader;
    private final NotificationWriter notificationWriter;
    private final NotificationSettingReader notificationSettingReader;
    private final NotificationSettingWriter notificationSettingWriter;

    // ========== 알림 생성 ==========

    @Transactional
    public void createBookNotification(NotificationBookMessage message, String userId) {
        notificationWriter.createBookNotification(message, userId);
    }

    @Transactional
    public void createChatNotification(NotificationChatMessage message) {
        notificationWriter.createChatNotification(message);
    }

    @Transactional
    public void createTradeNotification(NotificationTradeMessage message, String userId) {
        notificationWriter.createTradeNotification(message, userId);
    }

    // ========== 알림 조회 ==========

    public Page<NotificationResponse> getNotifications(String userId, Pageable pageable) {
        Page<Notification> notifications = notificationReader.getNotifications(userId, pageable);
        return notifications.map(NotificationResponse::new);
    }

    public Long getNotReadNumbers(String userId) {
        return notificationReader.getNotReadNumbers(userId);
    }

    // ========== 알림 읽음 처리 ==========

    @Transactional
    public void readNotification(Long notificationId) {
        Notification notification = notificationReader.findById(notificationId);
        notificationWriter.markAsRead(notification);
    }

    // ========== 알림 설정 생성 ==========

    @Transactional
    public void createNotificationSetting(String userId) {
        notificationSettingWriter.createNotificationSettings(userId);
    }

    // ========== 알림 설정 조회 ==========

    public List<NotificationSettingResponse> getAllNotificationSettings(String userId) {
        List<NotificationSetting> settings = notificationSettingReader.getAllNotificationSettings(userId);
        return settings.stream()
            .map(NotificationSettingResponse::new)
            .toList();
    }

    // ========== 알림 설정 토글 ==========

    @Transactional
    public void toggleNotificationSetting(Type type, String userId) {
        NotificationSetting setting = notificationSettingReader.findById(type, userId);
        notificationSettingWriter.toggleSendable(setting);
    }
}
