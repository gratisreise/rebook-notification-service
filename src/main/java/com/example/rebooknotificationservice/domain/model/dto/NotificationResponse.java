package com.example.rebooknotificationservice.domain.model.dto;

import com.example.rebooknotificationservice.domain.model.entity.Notification;
import com.example.rebooknotificationservice.common.enums.Type;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record NotificationResponse(
    Long notificationId,
    String relatedId,
    String message,
    Type type,
    LocalDateTime createdAt
) {
    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
            .notificationId(notification.getId())
            .relatedId(notification.getRelatedId())
            .message(notification.getMessage())
            .type(notification.getType())
            .createdAt(notification.getCreatedAt())
            .build();
    }
}
