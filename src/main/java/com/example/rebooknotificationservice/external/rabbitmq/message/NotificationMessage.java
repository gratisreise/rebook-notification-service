package com.example.rebooknotificationservice.external.rabbitmq.message;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationMessage {

    @NotBlank
    private String message;

    @NotBlank
    private String type;

}
