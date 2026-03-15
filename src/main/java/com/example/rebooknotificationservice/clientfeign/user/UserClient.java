package com.example.rebooknotificationservice.clientfeign.user;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user-service")
public interface UserClient {
    @GetMapping("/api/users/alarms/books")
    List<String> getUserIdsByCategory(@RequestParam String category);
}
