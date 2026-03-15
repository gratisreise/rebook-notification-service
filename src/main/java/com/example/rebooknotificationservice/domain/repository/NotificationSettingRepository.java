package com.example.rebooknotificationservice.domain.repository;

import com.example.rebooknotificationservice.domain.model.entity.NotificationSetting;
import com.example.rebooknotificationservice.domain.model.entity.compositekey.NotificationSettingId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, NotificationSettingId> {

    // userId로 조회
    List<NotificationSetting> findByNotificationSettingIdUserId(String userId);
}
