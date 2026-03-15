package com.example.rebooknotificationservice.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Type {
    CHAT("새로운 채팅이 왔습니다."),
    BOOK("새로운 도서가 등록되었습니다."),
    PAYMENT("결제가 완료되었습니다."),
    TRADE("새로운 거래가 등록되었습니다.")
    ;

    private final String message;
}
