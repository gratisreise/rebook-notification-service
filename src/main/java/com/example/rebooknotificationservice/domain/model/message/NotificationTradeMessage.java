package com.example.rebooknotificationservice.domain.model.message;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationTradeMessage extends NotificationMessage {
    @NotBlank
    String tradingId;

    @NotBlank
    String bookId;
}
