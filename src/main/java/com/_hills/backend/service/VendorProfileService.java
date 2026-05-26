package com._hills.backend.service;

import com._hills.backend.dto.VendorProfileRequest;
import com._hills.backend.entity.*;
import com._hills.backend.enums.DocumentType;
import com._hills.backend.enums.ProfileStatus;
import com._hills.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorProfileService {

    private final VendorProfileRepository profileRepository;
    private final VendorDocumentRepository documentRepository;
    private final UserRepository userRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public VendorProfile submitProfile(String email, VendorProfileRequest req,
                                       MultipartFile photo,
                                       MultipartFile businessLicense,
                                       MultipartFile nationalId,
                                       MultipartFile companyCertificate) throws IOException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create or update profile
        VendorProfile profile = profileRepository.findByUserEmail(email)
                .orElse(VendorProfile.builder().user(user).build());

        profile.setCompanyName(req.getCompanyName());
        profile.setContactPerson(req.getContactPerson());
        profile.setEmail(req.getEmail());
        profile.setPhone(req.getPhone());
        profile.setLocation(req.getLocation());
        profile.setRegistrationNo(req.getRegistrationNo());
        profile.setDescription(req.getDescription());
        profile.setStatus(ProfileStatus.PENDING);
        profile.setSubmittedAt(LocalDateTime.now());

        if (photo != null && !photo.isEmpty())
            profile.setPhotoUrl(saveFile(photo, "photos"));

        profile = profileRepository.save(profile);

        // Save documents
        saveDocument(profile, businessLicense, DocumentType.BUSINESS_LICENSE, true);
        saveDocument(profile, nationalId, DocumentType.NATIONAL_ID, true);
        saveDocument(profile, companyCertificate, DocumentType.COMPANY_CERTIFICATE, false);

        // Update user profile status
        user.setProfileStatus(ProfileStatus.PENDING);
        userRepository.save(user);

        return profile;
    }

    public VendorProfile getProfile(String email) {
        return profileRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public ProfileStatus getProfileStatus(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getProfileStatus();
    }

    public List<VendorProfile> getPendingProfiles() {
        return profileRepository.findByStatus(ProfileStatus.PENDING);
    }

    public List<VendorProfile> getAllVendorProfiles() {
        return profileRepository.findAll();
    }

    public VendorProfile getProfileById(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public void updateStatus(Long profileId, ProfileStatus status, String adminEmail) {
        VendorProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        profile.setStatus(status);
        profile.setReviewedAt(LocalDateTime.now());
        profile.setReviewedBy(admin);
        profileRepository.save(profile);

        // Sync user profile status
        User vendor = profile.getUser();
        vendor.setProfileStatus(status);
        userRepository.save(vendor);
    }

    private void saveDocument(VendorProfile profile, MultipartFile file,
                               DocumentType type, boolean required) throws IOException {
        if (file == null || file.isEmpty()) return;
        String url = saveFile(file, "documents");
        VendorDocument existing = documentRepository
                .findByVendorProfileIdAndDocumentType(profile.getId(), type)
                .orElse(VendorDocument.builder().vendorProfile(profile).documentType(type).isRequired(required).build());
        existing.setFileUrl(url);
        existing.setFileName(file.getOriginalFilename());
        documentRepository.save(existing);
    }

    private String saveFile(MultipartFile file, String subDir) throws IOException {
        Path dir = Paths.get(uploadDir, subDir);
        Files.createDirectories(dir);
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), dir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        return subDir + "/" + filename;
    }
}
