package com.example.rebooknotificationservice.service;

import com.example.rebooknotificationservice.feigns.BookClient;
import com.example.rebooknotificationservice.feigns.UserClient;
import com.example.rebooknotificationservice.model.message.NotificationBookMessage;
import com.example.rebooknotificationservice.model.message.NotificationChatMessage;
import com.example.rebooknotificationservice.model.message.NotificationMessage;
import com.example.rebooknotificationservice.model.message.NotificationTradeMessage;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
@Slf4j
public class SseService {

    // 클라이언트별 Emitter 관리
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final NotificationService notificationService;
    private final BookClient bookClient;
    private final UserClient userClient;

    public void receiveBookNotification(NotificationBookMessage message) {
        log.info("도서알림진행중");
        List<String> userIds = userClient.getUserIdsByCategory(message.getCategory());
        log.info("아이디받음");
        userIds.forEach(userId -> {
            notificationService.createBookNotification(message, userId);
            if(isConnected(userId)) sendNotification(message, userId);
        });
    }

    public void receiveTradeNotification(@Valid NotificationTradeMessage message) {
        long bookId = Long.parseLong(message.getBookId());
        List<String> userIds = bookClient.getUserIdsByBookId(bookId);
        log.info("거래알림진행중");
        userIds.forEach(userId -> {
            notificationService.createTradeNotification(message, userId);
            if(isConnected(userId)) sendNotification(message, userId);
        });
    }


    public void receiveChatNotification(@Valid NotificationChatMessage message) {
        log.info("채팅알람메세지 {}", message.toString());
        notificationService.createChatNotification(message);
        if(isConnected(message.getUserId())){
            sendNotification(message, message.getUserId());
        }
    }

    private void sendNotification(NotificationMessage message, String userId) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("notification")
                    .data(message.getMessage()));
            } catch (Exception e) {
                emitters.remove(userId);
            }
        }
    }

    private boolean isConnected(String userId){
        return emitters.containsKey(userId);
    }

    public SseEmitter connect(String userId) {
        SseEmitter emitter = new SseEmitter(60_000L);

        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        emitter.onError(e -> emitters.remove(userId));

        // 연결 직후 테스트 메시지 전송
        try {
            emitter.send(SseEmitter.event()
                .name("connected")
                .data("connected"));
            log.info("sse연결 성공!!");
        } catch (IOException e) {
            log.warn("sse연결 실패!!");
            sendErrorToClient(userId, e.getMessage());
        }

        return emitter;
    }

    public void sendErrorToClient(String userId, String errorMsg) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            emitter.completeWithError(new RuntimeException(errorMsg));
            emitters.remove(userId);
        }
    }


    @Scheduled(fixedRate = 15_000)
    public void sendHeartbeat() {
        for (Map.Entry<String, SseEmitter> entry : emitters.entrySet()) {
            try {
                entry.getValue().send(SseEmitter.event()
                    .name("heartbeat")
                    .data("ping"));
            } catch (Exception e) {
                // 예외 발생 시 해당 emitter 제거 (연결 끊김 처리)
                emitters.remove(entry.getKey());
                log.warn("Heartbeat 전송 실패, emitter 제거 userId={}", entry.getKey());
            }
        }
    }
}
