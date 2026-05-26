package com._hills.backend.repository;

import com._hills.backend.entity.VendorProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendorProductRepository extends JpaRepository<VendorProduct, Long> {
    List<VendorProduct> findByVendorId(Long vendorId);
    List<VendorProduct> findByVendorEmail(String email);
}
