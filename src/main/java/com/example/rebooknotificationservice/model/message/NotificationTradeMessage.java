package com.example.rebooknotificationservice.model.message;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
public class NotificationTradeMessage extends NotificationMessage {
    @NotBlank
    String tradingId;

    @NotBlank
    String bookId;
}
