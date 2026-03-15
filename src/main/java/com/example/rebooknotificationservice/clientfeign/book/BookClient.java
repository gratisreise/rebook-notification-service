package com.example.rebooknotificationservice.clientfeign.book;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="book-service")
public interface BookClient {
    @GetMapping("/alarms/books/{bookId}")
    List<String> getUserIdsByBookId(@PathVariable long bookId);
}
