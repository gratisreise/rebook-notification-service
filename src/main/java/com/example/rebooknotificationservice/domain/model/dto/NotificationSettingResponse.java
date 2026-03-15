package com.example.rebooknotificationservice.domain.model.dto;

import com.example.rebooknotificationservice.domain.model.entity.NotificationSetting;
import com.example.rebooknotificationservice.common.enums.Type;

public record NotificationSettingResponse(
    String userId,
    Type type,
    boolean sendable
) {
    public NotificationSettingResponse(NotificationSetting notificationSetting) {
        this(
            notificationSetting.getNotificationSettingId().getUserId(),
            notificationSetting.getNotificationSettingId().getType(),
            notificationSetting.isSendable()
        );
    }
}
