package com.eshoppingzone.review.controller;

import com.eshoppingzone.review.dto.ReviewRequest;
import com.eshoppingzone.review.dto.ReviewResponse;
import com.eshoppingzone.review.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse createReview(
            @Valid @RequestBody ReviewRequest request) {

        return reviewService.createReview(request);
    }

    @GetMapping("/{id}")
    public ReviewResponse getReview(
            @PathVariable Long id) {

        return reviewService.getReview(id);
    }

    @GetMapping("/product/{productId}")
    public List<ReviewResponse> getByProductId(
            @PathVariable Long productId) {

        return reviewService.getByProductId(productId);
    }

    @GetMapping("/customer/{customerId}")
    public List<ReviewResponse> getByCustomerId(
            @PathVariable Long customerId) {

        return reviewService.getByCustomerId(customerId);
    }
}