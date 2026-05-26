package com._hills.backend.repository;

import com._hills.backend.entity.VendorDocument;
import com._hills.backend.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VendorDocumentRepository extends JpaRepository<VendorDocument, Long> {
    List<VendorDocument> findByVendorProfileId(Long vendorProfileId);
    Optional<VendorDocument> findByVendorProfileIdAndDocumentType(Long vendorProfileId, DocumentType documentType);
}
