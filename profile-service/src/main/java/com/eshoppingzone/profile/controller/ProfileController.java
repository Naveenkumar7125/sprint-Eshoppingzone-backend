package com.eshoppingzone.profile.controller;

import com.eshoppingzone.profile.dto.ProfileRequest;
import com.eshoppingzone.profile.dto.ProfileResponse;
import com.eshoppingzone.profile.security.AuthenticatedUser;
import com.eshoppingzone.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Tag(
        name = "Profiles",
        description = "APIs for managing the authenticated user's profile"
)
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {

    private final ProfileService profileService;

    @Operation(
            summary = "Create my profile",
            description = "Creates a profile for the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Profile created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid profile data"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Profile already exists"
            )
    })
    @PostMapping
    public ResponseEntity<ProfileResponse> createProfile(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody ProfileRequest request
    ) {
        ProfileResponse response = profileService.createProfile(
                authenticatedUser.userId(),
                request
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Get my profile",
            description = "Retrieves the profile of the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Profile retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Profile not found"
            )
    })
    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getMyProfile(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        ProfileResponse response = profileService.getMyProfile(
                authenticatedUser.userId()
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update my profile",
            description = "Updates the profile of the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Profile updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid profile data"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Profile not found"
            )
    })
    @PutMapping("/me")
    public ResponseEntity<ProfileResponse> updateMyProfile(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody ProfileRequest request
    ) {
        ProfileResponse response = profileService.updateMyProfile(
                authenticatedUser.userId(),
                request
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete my profile",
            description = "Deletes the profile of the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Profile deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid JWT"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Profile not found"
            )
    })
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyProfile(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        profileService.deleteMyProfile(
                authenticatedUser.userId()
        );

        return ResponseEntity.noContent().build();
    }
}