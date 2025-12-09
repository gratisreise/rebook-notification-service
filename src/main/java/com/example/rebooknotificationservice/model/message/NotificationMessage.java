package com.example.rebooknotificationservice.model.message;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
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
