package com.example.rebooknotificationservice.domain.service.reader;

import com.example.rebooknotificationservice.domain.model.entity.NotificationSetting;
import com.example.rebooknotificationservice.domain.model.entity.compositekey.NotificationSettingId;
import com.example.rebooknotificationservice.common.enums.Type;
import com.example.rebooknotificationservice.domain.repository.NotificationSettingRepository;
import com.example.rebooknotificationservice.exception.NotificationException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationSettingReader {
    private final NotificationSettingRepository notificationSettingRepository;

    public List<NotificationSetting> getAllNotificationSettings(String userId) {
        return notificationSettingRepository.findByNotificationSettingIdUserId(userId);
    }

    public NotificationSetting findById(Type type, String userId) {
        NotificationSettingId settingId = new NotificationSettingId(userId, type);
        return notificationSettingRepository.findById(settingId)
            .orElseThrow(NotificationException::new);
    }
}
