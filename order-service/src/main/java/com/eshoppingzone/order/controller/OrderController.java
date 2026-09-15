package com.eshoppingzone.order.controller;

import com.eshoppingzone.order.dto.request.OrderCreateRequest;
import com.eshoppingzone.order.entity.OrderStatus;
import com.eshoppingzone.order.dto.response.OrderResponse;
import com.eshoppingzone.order.service.OrderService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.eshoppingzone.order.entity.PaymentStatus;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderCreateRequest request) {

        OrderResponse response = orderService.createOrder(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long orderId) {

        OrderResponse response = orderService.getOrderById(orderId);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<java.util.List<OrderResponse>> getOrdersByCustomer(
            @PathVariable Long customerId) {

        java.util.List<OrderResponse> orders =
                orderService.getOrdersByCustomer(customerId);

        return ResponseEntity.ok(orders);
    }
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {

        OrderResponse response =
                orderService.updateOrderStatus(orderId, status);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                orderService.cancelOrder(orderId)
        );
    }
    @PutMapping("/{orderId}/payment-status")
    public ResponseEntity<OrderResponse> updatePaymentStatus(
            @PathVariable Long orderId,
            @RequestParam PaymentStatus status) {

        return ResponseEntity.ok(
                orderService.updatePaymentStatus(orderId, status)
        );
    }
}