package com.eshoppingzone.payment.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eshoppingzone.payment.dto.request.CreatePaymentRequest;
import com.eshoppingzone.payment.dto.request.RefundPaymentRequest;
import com.eshoppingzone.payment.dto.response.PaymentResponse;
import com.eshoppingzone.payment.dto.response.RefundResponse;
import com.eshoppingzone.payment.enums.PaymentStatus;
import com.eshoppingzone.payment.service.PaymentService;
import com.eshoppingzone.payment.security.SecurityUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Service", description = "Payment & refund APIs")
public class PaymentController {

    private final PaymentService paymentService;
    private final SecurityUtils securityUtils;

    // ========== CREATE ==========

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Create a payment for an order")
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody CreatePaymentRequest request,
            @RequestHeader("Authorization") String bearerToken) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.createPayment(
                        request,
                        securityUtils.getCurrentUserId(),
                        bearerToken));
    }

    // ========== INITIATE / CONFIRM ==========

    @PostMapping("/{paymentId}/initiate")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Initiate a payment (wallet debit or COD pending)")
    public ResponseEntity<PaymentResponse> initiatePayment(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.initiatePayment(
                paymentId, securityUtils.getCurrentUserId()));
    }

    @PostMapping("/{paymentId}/confirm")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Confirm a payment (idempotent)")
    public ResponseEntity<PaymentResponse> confirmPayment(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.confirmPayment(
                paymentId, securityUtils.getCurrentUserId()));
    }

    // ========== READ ==========

    @GetMapping("/{paymentId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN', 'MERCHANT')")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPayment(
                paymentId,
                securityUtils.getCurrentUserId(),
                securityUtils.getCurrentRole()));
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN', 'MERCHANT')")
    @Operation(summary = "Get payment by order ID")
    public ResponseEntity<PaymentResponse> getPaymentByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrder(
                orderId,
                securityUtils.getCurrentUserId(),
                securityUtils.getCurrentRole()));
    }

    @GetMapping("/my-payments")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Get paginated list of the authenticated customer's payments")
    public ResponseEntity<Page<PaymentResponse>> getMyPayments(
            @RequestParam(required = false) PaymentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(paymentService.getMyPayments(
                securityUtils.getCurrentUserId(), status, pageable));
    }

    // ========== CANCEL ==========

    @PostMapping("/{paymentId}/cancel")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @Operation(summary = "Cancel a pending payment")
    public ResponseEntity<PaymentResponse> cancelPayment(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.cancelPayment(
                paymentId,
                securityUtils.getCurrentUserId(),
                securityUtils.getCurrentRole()));
    }

    // ========== REFUND ==========

    @PostMapping("/{paymentId}/refund")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @Operation(summary = "Refund a successful payment (partial or full)")
    public ResponseEntity<RefundResponse> refundPayment(
            @PathVariable Long paymentId,
            @Valid @RequestBody RefundPaymentRequest request) {
        return ResponseEntity.ok(paymentService.refundPayment(
                paymentId,
                request,
                securityUtils.getCurrentUserId(),
                securityUtils.getCurrentRole()));
    }
}