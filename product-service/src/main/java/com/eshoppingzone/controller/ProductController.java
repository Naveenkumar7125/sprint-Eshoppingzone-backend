package com.eshoppingzone.controller;

import com.eshoppingzone.dto.ProductCreateRequest;
import com.eshoppingzone.dto.ProductUpdateRequest;
import com.eshoppingzone.dto.ProductResponse;
import com.eshoppingzone.dto.ProductSummaryResponse;
import com.eshoppingzone.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductCreateRequest request) {

        ProductResponse response =
                productService.createProduct(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductSummaryResponse>> getAllProducts() {

        return ResponseEntity.ok(
                productService.getAllProducts()
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable
            @Positive(message = "Product ID must be positive")
            Long productId) {

        return ResponseEntity.ok(
                productService.getProductById(productId)
        );
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable
            @Positive(message = "Product ID must be positive")
            Long productId,

            @Valid @RequestBody ProductUpdateRequest request) {

        return ResponseEntity.ok(
                productService.updateProduct(
                        productId,
                        request
                )
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable
            @Positive(message = "Product ID must be positive")
            Long productId) {

        productService.deleteProduct(productId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductSummaryResponse>> searchProducts(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                productService.searchProducts(keyword)
        );
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductSummaryResponse>>
    getProductsByCategory(
            @PathVariable
            @Positive(message = "Category ID must be positive")
            Long categoryId) {

        return ResponseEntity.ok(
                productService.getProductsByCategory(categoryId)
        );
    }

    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<List<ProductSummaryResponse>>
    getProductsByMerchant(
            @PathVariable
            @Positive(message = "Merchant ID must be positive")
            Long merchantId) {

        return ResponseEntity.ok(
                productService.getProductsByMerchant(merchantId)
        );
    }
}