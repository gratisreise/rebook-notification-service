package com.example.rebooknotificationservice.model.message;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
public class NotificationBookMessage extends NotificationMessage {
    private String bookId;
    private String category;
}
