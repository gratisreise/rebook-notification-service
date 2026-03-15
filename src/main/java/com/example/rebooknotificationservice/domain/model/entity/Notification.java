package com.example.rebooknotificationservice.domain.model.entity;

import com.example.rebooknotificationservice.common.enums.Type;
import com.example.rebooknotificationservice.domain.model.message.NotificationBookMessage;
import com.example.rebooknotificationservice.domain.model.message.NotificationChatMessage;
import com.example.rebooknotificationservice.domain.model.message.NotificationTradeMessage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String userId; //알림을 받는 상대 id

    @Column(nullable = false, length = 50)
    private String message;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false)
    private boolean read;

    @Column(nullable = false, length = 50)
    private String relatedId; //

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    public Notification(NotificationBookMessage message, String userId) {
        this.userId = userId;
        this.message = message.getMessage();
        this.type = Type.valueOf(message.getType().toUpperCase());
        this.read = false;
        this.relatedId = message.getBookId();
    }

    public Notification(NotificationTradeMessage message, String userId) {
        this.userId = userId;
        this.message = message.getMessage();
        this.type = Type.valueOf(message.getType().toUpperCase());
        this.read = false;
        this.relatedId = message.getTradingId();
    }

    public Notification(NotificationChatMessage message) {
        this.userId = message.getUserId();
        this.message = message.getMessage();
        this.type = Type.valueOf(message.getType().toUpperCase());
        this.read = false;
        this.relatedId = message.getRoomId();
    }
}
