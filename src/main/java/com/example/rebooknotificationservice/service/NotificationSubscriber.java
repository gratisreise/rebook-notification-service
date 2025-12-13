package com.example.rebooknotificationservice.service;

import com.example.rebooknotificationservice.model.message.NotificationBookMessage;
import com.example.rebooknotificationservice.model.message.NotificationChatMessage;
import com.example.rebooknotificationservice.model.message.NotificationMessage;
import com.example.rebooknotificationservice.model.message.NotificationTradeMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationSubscriber {

    private final ObjectMapper objectMapper;
    private final SseService sseService;


    public void onMessage(String payload) {
        try {
            NotificationMessage message = objectMapper.readValue(payload, NotificationMessage.class);
            String type = message.getType();

            switch(type){
                case "TRADE" -> sendTradeNotification(payload);
                case "BOOK" -> sendBookNotification(payload);
                case "CHAT" -> sendChatNotification(payload);
                default -> log.error("레디스에서 알림 메세지 처리 오류");
            }

        } catch (Exception e) {
            Sentry.captureException(e);
            log.error("Failed to handle redis message", e);
        }
    }

    private void sendTradeNotification(String payload) throws JsonProcessingException {
        NotificationTradeMessage message = objectMapper.readValue(payload, NotificationTradeMessage.class);
        sseService.receiveTradeNotification(message);
    }
    private void sendBookNotification(String payload) throws JsonProcessingException {
        NotificationBookMessage message = objectMapper.readValue(payload, NotificationBookMessage.class);
        sseService.receiveBookNotification(message);
    }
    private void sendChatNotification(String payload) throws JsonProcessingException {
        NotificationChatMessage message = objectMapper.readValue(payload, NotificationChatMessage.class);
        sseService.receiveChatNotification(message);
    }
}
