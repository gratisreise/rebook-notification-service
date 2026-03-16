package com.example.rebooknotificationservice.exception;

import com.rebook.common.core.exception.BusinessException;
import com.rebook.common.core.exception.ErrorCode;

/**
 * 알림 도메인 관련 예외
 */
public class NotificationException extends BusinessException {
    public NotificationException() {
        super(ErrorCode.UNKNOWN_ERROR);
    }

    public NotificationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
