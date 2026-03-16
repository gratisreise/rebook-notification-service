package com.example.rebooknotificationservice.domain.model.dto;

import com.example.rebooknotificationservice.domain.model.entity.NotificationSetting;
import com.example.rebooknotificationservice.common.enums.Type;
import lombok.Builder;

@Builder
public record NotificationSettingResponse(
    String userId,
    Type type,
    boolean sendable
) {
    public static NotificationSettingResponse from(NotificationSetting notificationSetting) {
        return NotificationSettingResponse.builder()
            .userId(notificationSetting.getNotificationSettingId().getUserId())
            .type(notificationSetting.getNotificationSettingId().getType())
            .sendable(notificationSetting.isSendable())
            .build();
    }
}
