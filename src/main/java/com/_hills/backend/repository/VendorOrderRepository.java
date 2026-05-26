package com._hills.backend.repository;

import com._hills.backend.entity.VendorOrder;
import com._hills.backend.enums.VendorStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendorOrderRepository extends JpaRepository<VendorOrder, String> {
    List<VendorOrder> findByVendorEmail(String email);
    List<VendorOrder> findByVendorId(Long vendorId);
    List<VendorOrder> findByVendorEmailAndVendorStatus(String email, VendorStatus vendorStatus);
}
