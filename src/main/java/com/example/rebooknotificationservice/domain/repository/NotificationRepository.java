package com.example.rebooknotificationservice.domain.repository;

import com.example.rebooknotificationservice.domain.model.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByUserId(String userId, Pageable pageable);
    Page<Notification> findByUserIdAndReadFalse(String userId, Pageable pageable);
    Long countByUserIdAndReadFalse(String userId);
}
