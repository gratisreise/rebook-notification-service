package com.example.rebooknotificationservice.domain.model.dto;

import com.example.rebooknotificationservice.domain.model.entity.Notification;
import com.example.rebooknotificationservice.common.enums.Type;
import java.time.LocalDateTime;

public record NotificationResponse(
    Long notificationId,
    String relatedId,
    String message,
    Type type,
    LocalDateTime createdAt
) {
    public NotificationResponse(Notification notification) {
        this(
            notification.getId(),
            notification.getRelatedId(),
            notification.getMessage(),
            notification.getType(),
            notification.getCreatedAt()
        );
    }
}
