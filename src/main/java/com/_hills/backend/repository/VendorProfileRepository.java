package com._hills.backend.repository;

import com._hills.backend.entity.VendorProfile;
import com._hills.backend.enums.ProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VendorProfileRepository extends JpaRepository<VendorProfile, Long> {
    Optional<VendorProfile> findByUserEmail(String email);
    Optional<VendorProfile> findByUserId(Long userId);
    List<VendorProfile> findByStatus(ProfileStatus status);
}
