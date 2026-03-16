package com.example.rebooknotificationservice.domain.service.reader;

import com.example.rebooknotificationservice.domain.model.entity.Notification;
import com.example.rebooknotificationservice.domain.repository.NotificationRepository;
import com.example.rebooknotificationservice.exception.NotificationException;
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
            .orElseThrow(NotificationException::new);
    }


    public Long getNotReadNumbers(String userId) {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }
}
