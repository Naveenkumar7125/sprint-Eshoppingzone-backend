package com.eshoppingzone.notification.controller;

import com.eshoppingzone.notification.dto.NotificationRequest;
import com.eshoppingzone.notification.dto.NotificationResponse;
import com.eshoppingzone.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationResponse createNotification(
            @Valid @RequestBody NotificationRequest request) {

        return notificationService.createNotification(request);
    }

    @GetMapping("/{id}")
    public NotificationResponse getNotification(
            @PathVariable Long id) {

        return notificationService.getNotification(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<NotificationResponse> getByCustomerId(
            @PathVariable Long customerId) {

        return notificationService.getByCustomerId(customerId);
    }
}