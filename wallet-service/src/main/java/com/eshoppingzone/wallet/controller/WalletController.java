package com.eshoppingzone.wallet.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eshoppingzone.wallet.dto.request.AdminCustomerTransferRequest;
import com.eshoppingzone.wallet.dto.request.CreateWalletRequest;
import com.eshoppingzone.wallet.dto.request.CustomerAdminTransferRequest;
import com.eshoppingzone.wallet.dto.request.WalletDebitRequest;
import com.eshoppingzone.wallet.dto.request.WalletTopUpRequest;
import com.eshoppingzone.wallet.dto.response.TransferResponse;
import com.eshoppingzone.wallet.dto.response.WalletBalanceResponse;
import com.eshoppingzone.wallet.dto.response.WalletResponse;
import com.eshoppingzone.wallet.dto.response.WalletTransactionResponse;
import com.eshoppingzone.wallet.enums.TransactionType;
import com.eshoppingzone.wallet.security.SecurityUtils;
import com.eshoppingzone.wallet.service.WalletService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
@Tag(name = "Wallet Service", description = "Wallet management APIs")
public class WalletController {

    private final WalletService walletService;
    private final SecurityUtils securityUtils;

    // ========== CREATE ==========

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @Operation(summary = "Create wallet for authenticated user")
    public ResponseEntity<WalletResponse> createWallet(@Valid @RequestBody CreateWalletRequest request) {
        Long userId = securityUtils.getCurrentUserId();
        String role = securityUtils.getCurrentRole();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(walletService.createWallet(request, userId, role));
    }

    // ========== READ ==========

    @GetMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @Operation(summary = "Get authenticated user's wallet")
    public ResponseEntity<WalletResponse> getMyWallet() {
        return ResponseEntity.ok(
                walletService.getMyWallet(securityUtils.getCurrentUserId(), securityUtils.getCurrentRole()));
    }

    @GetMapping("/balance")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @Operation(summary = "Get authenticated user's wallet balance")
    public ResponseEntity<WalletBalanceResponse> getBalance() {
        return ResponseEntity.ok(
                walletService.getBalance(securityUtils.getCurrentUserId(), securityUtils.getCurrentRole()));
    }

    // ========== TOP-UP ==========

    @PostMapping("/top-up")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Top up customer wallet")
    public ResponseEntity<WalletResponse> topUp(@Valid @RequestBody WalletTopUpRequest request) {
        return ResponseEntity.ok(walletService.topUp(request, securityUtils.getCurrentUserId()));
    }

    // ========== INTERNAL: DEBIT ==========

    @PostMapping("/debit")
    @PreAuthorize("hasRole('INTERNAL_SERVICE')")
    @Operation(summary = "Internal - Debit a wallet")
    public ResponseEntity<TransferResponse> debit(@Valid @RequestBody WalletDebitRequest request) {
        return ResponseEntity.ok(walletService.debitWallet(request));
    }

    // ========== INTERNAL: ADMIN CREDIT ==========

    @PostMapping("/admin/credit")
    @PreAuthorize("hasRole('INTERNAL_SERVICE')")
    @Operation(summary = "Internal - Credit admin wallet")
    public ResponseEntity<TransferResponse> creditAdmin(@Valid @RequestBody WalletDebitRequest request) {
        return ResponseEntity.ok(walletService.creditAdminWallet(request));
    }

    // ========== INTERNAL: CUSTOMER → ADMIN ==========

    @PostMapping("/internal/transfer/customer-to-admin")
    @PreAuthorize("hasRole('INTERNAL_SERVICE')")
    @Operation(summary = "Internal - Transfer customer wallet → admin wallet")
    public ResponseEntity<TransferResponse> transferCustomerToAdmin(
            @Valid @RequestBody CustomerAdminTransferRequest request) {
        return ResponseEntity.ok(walletService.transferCustomerToAdmin(request));
    }

    // ========== INTERNAL: ADMIN → CUSTOMER ==========

    @PostMapping("/internal/transfer/admin-to-customer")
    @PreAuthorize("hasRole('INTERNAL_SERVICE')")
    @Operation(summary = "Internal - Transfer admin wallet → customer wallet (refund)")
    public ResponseEntity<TransferResponse> transferAdminToCustomer(
            @Valid @RequestBody AdminCustomerTransferRequest request) {
        return ResponseEntity.ok(walletService.transferAdminToCustomer(request));
    }

    // ========== TRANSACTIONS ==========

    @GetMapping("/transactions")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @Operation(summary = "Get paginated wallet transactions")
    public ResponseEntity<Page<WalletTransactionResponse>> getTransactions(
            @RequestParam(required = false) TransactionType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(walletService.getTransactions(
                securityUtils.getCurrentUserId(), securityUtils.getCurrentRole(), type, pageable));
    }

    @GetMapping("/transactions/{transactionId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @Operation(summary = "Get a specific transaction")
    public ResponseEntity<WalletTransactionResponse> getTransaction(@PathVariable Long transactionId) {
        return ResponseEntity.ok(walletService.getTransaction(
                transactionId, securityUtils.getCurrentUserId(), securityUtils.getCurrentRole()));
    }

    // ========== ADMIN ==========

    @PatchMapping("/{walletId}/block")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Block a wallet (admin)")
    public ResponseEntity<Void> block(@PathVariable Long walletId) {
        walletService.blockWallet(walletId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{walletId}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activate a wallet (admin)")
    public ResponseEntity<Void> activate(@PathVariable Long walletId) {
        walletService.activateWallet(walletId);
        return ResponseEntity.noContent().build();
    }
}