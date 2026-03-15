package com.example.rebooknotificationservice.domain.service;

import com.example.rebooknotificationservice.domain.model.entity.Notification;
import com.example.rebooknotificationservice.domain.repository.NotificationRepository;
import com.example.rebooknotificationservice.exception.CMissingDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationReader {
    private final NotificationRepository notificationRepository;

    public Page<Notification> getNotifications(String userId, Pageable pageable){
        return notificationRepository.findByUserIdAndReadFalse(userId, pageable);
    }

    public Notification findById(Long id){
        return notificationRepository.findById(id)
            .orElseThrow(CMissingDataException::new);
    }


    public Long getNotReadNumbers(String userId) {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }
}
