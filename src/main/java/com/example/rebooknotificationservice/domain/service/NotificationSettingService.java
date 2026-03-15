package com.example.rebooknotificationservice.domain.service;

import com.example.rebooknotificationservice.domain.model.dto.NotificationSettingResponse;
import com.example.rebooknotificationservice.domain.model.entity.NotificationSetting;
import com.example.rebooknotificationservice.domain.model.entity.compositekey.NotificationSettingId;
import com.example.rebooknotificationservice.common.enums.Type;
import com.example.rebooknotificationservice.domain.repository.NotificationSettingRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationSettingService {
    private final NotificationSettingRepository notificationSettingRepository;
    private final NotificationSettingReader notificationSettingReader;


    @Transactional
    public void createNotificationSetting(String userId) {
        for(Type type : Type.values()){
            NotificationSettingId settingId = new NotificationSettingId(userId, type);
            NotificationSetting setting = new NotificationSetting(settingId);
            notificationSettingRepository.save(setting);
        }
    }

    public List<NotificationSettingResponse> getAllNotificationSettings(String userId) {
        return notificationSettingReader.getAllNotificationSettings(userId)
            .stream().map(NotificationSettingResponse::new)
            .toList();
    }

    @Transactional
    public void toggleNotificationSetting(Type type, String userId) {
        NotificationSetting setting = notificationSettingReader.findById(type, userId);
        boolean change = !setting.isSendable();
        setting.setSendable(change);
    }
}
