package com.eshoppingzone.delivery.controller;

import com.eshoppingzone.delivery.dto.request.AssignDeliveryPartnerRequest;
import com.eshoppingzone.delivery.dto.request.CreateDeliveryRequest;
import com.eshoppingzone.delivery.dto.request.UpdateDeliveryStatusRequest;
import com.eshoppingzone.delivery.dto.response.DeliveryResponse;
import com.eshoppingzone.delivery.service.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryResponse createDelivery(
            @Valid @RequestBody CreateDeliveryRequest request) {

        return deliveryService.createDelivery(request);
    }

    @GetMapping("/{id}")
    public DeliveryResponse getDelivery(@PathVariable Long id) {

        return deliveryService.getDelivery(id);
    }

    @GetMapping("/tracking/{trackingNumber}")
    public DeliveryResponse getDeliveryByTrackingNumber(
            @PathVariable String trackingNumber) {

        return deliveryService.getDeliveryByTrackingNumber(trackingNumber);
    }

    @GetMapping("/order/{orderId}")
    public DeliveryResponse getDeliveryByOrderId(
            @PathVariable Long orderId) {

        return deliveryService.getDeliveryByOrderId(orderId);
    }

    @PutMapping("/{id}/status")
    public DeliveryResponse updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDeliveryStatusRequest request) {

        return deliveryService.updateStatus(id, request);
    }

    @PutMapping("/{id}/partner")
    public DeliveryResponse assignDeliveryPartner(
            @PathVariable Long id,
            @Valid @RequestBody AssignDeliveryPartnerRequest request) {

        return deliveryService.assignDeliveryPartner(id, request);
    }
}