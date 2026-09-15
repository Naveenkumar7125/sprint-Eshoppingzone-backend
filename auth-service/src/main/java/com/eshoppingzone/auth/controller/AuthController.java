package com.eshoppingzone.auth.controller;

import com.eshoppingzone.auth.dto.AuthResponse;
import com.eshoppingzone.auth.dto.LoginRequest;
import com.eshoppingzone.auth.dto.ProvisionUserRequest;
import com.eshoppingzone.auth.dto.RefreshTokenRequest;
import com.eshoppingzone.auth.dto.RegisterRequest;
import com.eshoppingzone.auth.exception.BadRequestException;
import com.eshoppingzone.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "Registration, login, token refresh, and user provisioning APIs"
)
public class AuthController {

    private final AuthService authService;

    @Value("${auth.provisioning-key}")
    private String provisioningKey;

    @Operation(
            summary = "Register a new user",
            description = "Registers a new CUSTOMER or MERCHANT account."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User already exists"
            )
    })
    
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }
    

    @Operation(
            summary = "Login user",
            description = "Authenticates a user and returns access and refresh tokens."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token using a valid refresh token."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token refreshed successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid or expired refresh token"
            )
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        AuthResponse response = authService.refreshAccessToken(request);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Provision a privileged user",
            description = "Creates an ADMIN or DELIVERY_AGENT account using a provisioning key."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Privileged user created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or provisioning key"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User already exists"
            )
    })
    @PostMapping("/provision")
    public ResponseEntity<AuthResponse> provisionUser(
            @RequestHeader(
                    value = "X-Provisioning-Key",
                    required = false
            ) String requestProvisioningKey,
            @Valid @RequestBody ProvisionUserRequest request
    ) {
        if (provisioningKey == null
                || !provisioningKey.equals(requestProvisioningKey)) {

            throw new BadRequestException("Invalid provisioning key");
        }

        AuthResponse response = authService.provisionUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}