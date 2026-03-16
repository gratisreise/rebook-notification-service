package com.example.rebooknotificationservice.domain.service.writer;

import com.example.rebooknotificationservice.domain.model.entity.NotificationSetting;
import com.example.rebooknotificationservice.domain.model.entity.compositekey.NotificationSettingId;
import com.example.rebooknotificationservice.common.enums.Type;
import com.example.rebooknotificationservice.domain.repository.NotificationSettingRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationSettingWriter {
    private final NotificationSettingRepository notificationSettingRepository;

    public List<NotificationSetting> createNotificationSettings(String userId) {
        List<NotificationSetting> settings = new ArrayList<>();
        for (Type type : Type.values()) {
            NotificationSettingId settingId = new NotificationSettingId(userId, type);
            NotificationSetting setting = new NotificationSetting(settingId);
            settings.add(notificationSettingRepository.save(setting));
        }
        return settings;
    }

    public void toggleSendable(NotificationSetting setting) {
        setting.setSendable(!setting.isSendable());
    }
}
