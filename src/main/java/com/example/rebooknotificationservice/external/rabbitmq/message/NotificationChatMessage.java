package com.example.rebooknotificationservice.external.rabbitmq.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationChatMessage extends NotificationMessage {
    private String userId;
    private String roomId;
}
