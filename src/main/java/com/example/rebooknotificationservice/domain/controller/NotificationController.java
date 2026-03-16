package com.example.rebooknotificationservice.domain.controller;

import com.example.rebooknotificationservice.common.enums.Type;
import com.example.rebooknotificationservice.domain.model.dto.NotificationResponse;
import com.example.rebooknotificationservice.domain.model.dto.NotificationSettingResponse;
import com.example.rebooknotificationservice.domain.service.NotificationService;
import com.rebook.common.auth.PassportUser;
import com.rebook.common.core.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    // ========== 알림 조회 ==========

    @GetMapping("/me")
    @Operation(summary = "내 알림 목록 조회")
    public ResponseEntity<SuccessResponse<Page<NotificationResponse>>> getNotifications(
        @PassportUser String userId,
        @PageableDefault Pageable pageable) {
        Page<NotificationResponse> notifications = notificationService.getNotifications(userId, pageable);
        return SuccessResponse.toOk(notifications);
    }

    @GetMapping("/me/numbers")
    @Operation(summary = "읽지 않은 알림 개수")
    public ResponseEntity<SuccessResponse<Long>> getNotReadNumbers(
        @PassportUser String userId) {
        Long count = notificationService.getNotReadNumbers(userId);
        return SuccessResponse.toOk(count);
    }

    // ========== 알림 읽음 처리 ==========

    @PatchMapping("/{notificationId}")
    @Operation(summary = "알림 읽음")
    public ResponseEntity<SuccessResponse<Void>> readNotification(@PathVariable Long notificationId) {
        notificationService.readNotification(notificationId);
        return SuccessResponse.toNoContent();
    }

    // ========== 알림 설정 ==========

    @GetMapping("/me/settings")
    @Operation(summary = "내 알림 설정 조회")
    public ResponseEntity<SuccessResponse<List<NotificationSettingResponse>>> getNotificationSettings(
        @PassportUser String userId) {
        List<NotificationSettingResponse> settings = notificationService.getAllNotificationSettings(userId);
        return SuccessResponse.toOk(settings);
    }

    @PostMapping("/me/settings")
    @Operation(summary = "알림 설정 생성")
    public ResponseEntity<SuccessResponse<Void>> createNotificationSetting(@PassportUser String userId) {
        notificationService.createNotificationSetting(userId);
        return SuccessResponse.toNoContent();
    }

    @PatchMapping("/settings")
    @Operation(summary = "알림 설정 변경")
    public ResponseEntity<SuccessResponse<Void>> toggleNotificationSetting(
        @RequestParam Type type,
        @PassportUser String userId) {
        notificationService.toggleNotificationSetting(type, userId);
        return SuccessResponse.toNoContent();
    }
}
