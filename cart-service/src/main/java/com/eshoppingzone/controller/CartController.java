package com.eshoppingzone.cotroller;

import com.eshoppingzone.dto.AddCartItemRequest;
import com.eshoppingzone.dto.UpdateCartItemRequest;
import com.eshoppingzone.dto.CartResponse;
import com.eshoppingzone.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@Validated
public class CartController {

    private final CartService cartService;

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<CartResponse> getCartByCustomerId(
            @PathVariable
            @Positive(message = "Customer ID must be positive")
            Long customerId) {

        return ResponseEntity.ok(
                cartService.getCartByCustomerId(customerId)
        );
    }

    @PostMapping("/customer/{customerId}/items")
    public ResponseEntity<CartResponse> addItem(
            @PathVariable
            @Positive(message = "Customer ID must be positive")
            Long customerId,

            @Valid
            @RequestBody AddCartItemRequest request) {

        return ResponseEntity.ok(
                cartService.addItemToCart(customerId, request)
        );
    }

    @PutMapping("/customer/{customerId}/items/{productId}")
    public ResponseEntity<CartResponse> updateItem(
            @PathVariable
            @Positive(message = "Customer ID must be positive")
            Long customerId,

            @PathVariable
            @Positive(message = "Product ID must be positive")
            Long productId,

            @Valid
            @RequestBody UpdateCartItemRequest request) {

        return ResponseEntity.ok(
                cartService.updateCartItem(
                        customerId,
                        productId,
                        request
                )
        );
    }

    @DeleteMapping("/customer/{customerId}/items/{productId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable
            @Positive(message = "Customer ID must be positive")
            Long customerId,

            @PathVariable
            @Positive(message = "Product ID must be positive")
            Long productId) {

        cartService.removeCartItem(customerId, productId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/customer/{customerId}/items")
    public ResponseEntity<Void> clearCart(
            @PathVariable
            @Positive(message = "Customer ID must be positive")
            Long customerId) {

        cartService.clearCart(customerId);

        return ResponseEntity.noContent().build();
    }
}