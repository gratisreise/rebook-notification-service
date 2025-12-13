package com.example.rebooknotificationservice.model.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class NotificationMessageFactory {
    private final Map<String,  NotificationMessage> notificationMessageMap = new HashMap<>();

    public NotificationMessageFactory(List<NotificationMessage> notificationMessages){
        for(NotificationMessage notificationMessage : notificationMessages){
            if(notificationMessage != null){
                notificationMessageMap.put(notificationMessage.getType(), notificationMessage);
            }
        }
    }

    public NotificationMessage getNotificationMessage(String type){
        return notificationMessageMap.get(type);
    }
}
