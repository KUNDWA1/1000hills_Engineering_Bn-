package com._hills.backend.service;

import com._hills.backend.entity.VendorOrder;
import com._hills.backend.enums.VendorStatus;
import com._hills.backend.repository.VendorOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorOrderService {

    private final VendorOrderRepository vendorOrderRepository;

    public List<VendorOrder> getMyOrders(String email) {
        return vendorOrderRepository.findByVendorEmail(email);
    }

    public VendorOrder getOrderById(String id, String email) {
        VendorOrder order = vendorOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if (!order.getVendor().getEmail().equals(email))
            throw new RuntimeException("Unauthorized");
        return order;
    }

    public VendorOrder accept(String id, String email) {
        VendorOrder order = getOrderById(id, email);
        order.setVendorStatus(VendorStatus.ACCEPTED);
        return vendorOrderRepository.save(order);
    }

    public VendorOrder reject(String id, String email) {
        VendorOrder order = getOrderById(id, email);
        order.setVendorStatus(VendorStatus.REJECTED);
        return vendorOrderRepository.save(order);
    }
}
