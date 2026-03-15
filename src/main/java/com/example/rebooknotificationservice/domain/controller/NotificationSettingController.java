package com.example.rebooknotificationservice.domain.controller;

import com.example.rebooknotificationservice.common.CommonResult;
import com.example.rebooknotificationservice.common.ListResult;
import com.example.rebooknotificationservice.common.ResponseService;
import com.example.rebooknotificationservice.domain.model.dto.NotificationSettingResponse;
import com.example.rebooknotificationservice.common.enums.Type;
import com.example.rebooknotificationservice.domain.service.NotificationSettingService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationSettingController {

    private final NotificationSettingService notificationSettingService;

    @GetMapping("/me/settings")
    @Operation(summary = "내 알림 설정조회")
    public ListResult<NotificationSettingResponse> getNotificationSettings(
        @RequestHeader("X-User-Id") String userId) {
        return ResponseService.getListResult(
            notificationSettingService.getAllNotificationSettings(userId));
    }

    @PostMapping("/me/settings/{userId}")
    @Operation(summary = "알림설정생성")
    public CommonResult createNotificationsSettings(@PathVariable String userId){
        notificationSettingService.createNotificationSetting(userId);
        return ResponseService.getSuccessResult();
    }

    @PatchMapping("/settings")
    @Operation(summary = "알림 설정 변경")
    public CommonResult toggleNotificationSetting(@RequestParam Type type,
        @RequestHeader("X-User-Id") String userId) {
        notificationSettingService.toggleNotificationSetting(type, userId);
        return ResponseService.getSuccessResult();
    }
}
