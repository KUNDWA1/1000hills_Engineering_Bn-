package com._hills.backend.controller;

import com._hills.backend.dto.VendorProfileRequest;
import com._hills.backend.entity.VendorProfile;
import com._hills.backend.enums.ProfileStatus;
import com._hills.backend.service.VendorProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/vendor/profile")
@RequiredArgsConstructor
public class VendorProfileController {

    private final VendorProfileService profileService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<VendorProfile> submit(
            @AuthenticationPrincipal UserDetails user,
            @Valid @ModelAttribute VendorProfileRequest req,
            @RequestPart(required = false) MultipartFile photo,
            @RequestPart(required = false) MultipartFile businessLicense,
            @RequestPart(required = false) MultipartFile nationalId,
            @RequestPart(required = false) MultipartFile companyCertificate) throws IOException {

        return ResponseEntity.ok(profileService.submitProfile(
                user.getUsername(), req, photo, businessLicense, nationalId, companyCertificate));
    }

    @GetMapping
    public ResponseEntity<VendorProfile> getProfile(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(profileService.getProfile(user.getUsername()));
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getStatus(@AuthenticationPrincipal UserDetails user) {
        ProfileStatus status = profileService.getProfileStatus(user.getUsername());
        return ResponseEntity.ok(Map.of("profileStatus", status.name()));
    }
}
