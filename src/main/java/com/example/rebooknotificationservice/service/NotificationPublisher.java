package com.example.rebooknotificationservice.service;

import com.example.rebooknotificationservice.model.message.NotificationBookMessage;
import com.example.rebooknotificationservice.model.message.NotificationChatMessage;
import com.example.rebooknotificationservice.model.message.NotificationMessage;
import com.example.rebooknotificationservice.model.message.NotificationTradeMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationPublisher {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "book.notification.queue")
    public void onBook(NotificationBookMessage message) throws JsonProcessingException {
        publish(message);
    }

    @RabbitListener(queues = "trade.notification.queue")
    public void onTrade(@Valid NotificationTradeMessage message) {
        publish(message);
    }

    @RabbitListener(queues = "chat.notification.queue")
    public void onChat(@Valid NotificationChatMessage message) {
        publish(message);
    }

    private void publish(NotificationMessage message) {
        try{
            String payload = objectMapper.writeValueAsString(message);
            redisTemplate.convertAndSend("notification", payload);
        } catch(Exception e){
            log.error("레디스 메세지 발행 실패");
        }
    }

}
