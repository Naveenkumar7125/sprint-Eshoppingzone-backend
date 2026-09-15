package com.eshoppingzone.inventory.controller;

import com.eshoppingzone.inventory.dto.InventoryResponse;
import com.eshoppingzone.inventory.dto.StockReservationRequest;
import com.eshoppingzone.inventory.dto.StockUpdateRequest;
import com.eshoppingzone.inventory.entity.Inventory;
import com.eshoppingzone.inventory.service.InventoryService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(
            @RequestBody Inventory inventory) {

        Inventory saved = inventoryService.createInventory(inventory);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(saved));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getInventory(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                inventoryService.getInventoryByProductId(productId)
        );
    }

    @PostMapping("/reserve")
    public ResponseEntity<InventoryResponse> reserveStock(
            @Valid @RequestBody StockReservationRequest request) {

        return ResponseEntity.ok(
                toResponse(
                        inventoryService.reserveStock(
                                request.getOrderId(),
                                request.getProductId(),
                                request.getQuantity()
                        )
                )
        );
    }

    @PostMapping("/release")
    public ResponseEntity<?> releaseStock(
            @Valid @RequestBody StockUpdateRequest request) {

        return ResponseEntity.ok(
                inventoryService.releaseStock(
                        request.getOrderId(),
                        request.getProductId(),
                        request.getQuantity()
                )
        );
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmStock(
            @Valid @RequestBody StockUpdateRequest request) {

        return ResponseEntity.ok(
                inventoryService.confirmStock(
                        request.getOrderId(),
                        request.getProductId(),
                        request.getQuantity()
                )
        );
    }

    @PostMapping("/add-stock")
    public ResponseEntity<InventoryResponse> addStock(
            @Valid @RequestBody StockUpdateRequest request) {

        return ResponseEntity.ok(
                toResponse(inventoryService.addStock(
                        request.getProductId(),
                        request.getQuantity()
                ))
        );
    }

    private InventoryResponse toResponse(Inventory inventory) {

        InventoryResponse response = new InventoryResponse();

        response.setId(inventory.getId());
        response.setProductId(inventory.getProductId());
        response.setAvailableQuantity(inventory.getAvailableQuantity());
        response.setReservedQuantity(inventory.getReservedQuantity());
        response.setSoldQuantity(inventory.getSoldQuantity());
        response.setReorderLevel(inventory.getReorderLevel());
        response.setCreatedAt(inventory.getCreatedAt());
        response.setUpdatedAt(inventory.getUpdatedAt());

        return response;
    }
}