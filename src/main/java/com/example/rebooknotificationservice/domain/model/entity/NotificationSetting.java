package com.example.rebooknotificationservice.domain.model.entity;

import com.example.rebooknotificationservice.domain.model.entity.compositekey.NotificationSettingId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSetting {
    @EmbeddedId
    private NotificationSettingId notificationSettingId;

    @Column(nullable = false)
    private boolean sendable;

    public NotificationSetting(NotificationSettingId settingId) {
       this.notificationSettingId = settingId;
       this.sendable = true;
    }
}
