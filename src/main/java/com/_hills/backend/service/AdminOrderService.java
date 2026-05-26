package com._hills.backend.service;

import com._hills.backend.dto.AssignOrderRequest;
import com._hills.backend.dto.UpdateOrderStatusRequest;
import com._hills.backend.entity.*;
import com._hills.backend.enums.OrderStatus;
import com._hills.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final CustomerOrderRepository customerOrderRepository;
    private final VendorOrderRepository vendorOrderRepository;
    private final UserRepository userRepository;
    private final VendorProfileRepository vendorProfileRepository;

    public List<CustomerOrder> getAllOrders() {
        return customerOrderRepository.findAll();
    }

    public CustomerOrder getOrderById(String id) {
        return customerOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public CustomerOrder assignOrder(String orderId, AssignOrderRequest req) {
        CustomerOrder customerOrder = customerOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // vendorId from frontend is VendorProfile.id — resolve to User
        VendorProfile vendorProfile = vendorProfileRepository.findById(req.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor profile not found"));
        User vendor = vendorProfile.getUser();

        // Build vendor order — NO customer info
        VendorOrder vendorOrder = VendorOrder.builder()
                .id(orderId)
                .customerOrder(customerOrder)
                .vendor(vendor)
                .deliveryDate(req.getDeliveryDate())
                .description(req.getDescription())
                .total(req.getProducts().stream()
                        .map(p -> p.getUnitPrice().multiply(java.math.BigDecimal.valueOf(p.getQty())))
                        .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add))
                .build();

        List<VendorOrderItem> items = req.getProducts().stream().map(p ->
                VendorOrderItem.builder()
                        .vendorOrder(vendorOrder)
                        .productName(p.getProductName())
                        .qty(p.getQty())
                        .unitPrice(p.getUnitPrice())
                        .build()
        ).toList();

        vendorOrder.setItems(items);
        vendorOrderRepository.save(vendorOrder);

        // Update customer order
        customerOrder.setStatus(OrderStatus.ASSIGNED);
        customerOrder.setVendor(vendor);
        customerOrder.setVendorName(vendor.getName());
        customerOrder.setDeliveryDate(req.getDeliveryDate());
        customerOrder.setAssignedAt(LocalDateTime.now());

        return customerOrderRepository.save(customerOrder);
    }

    @Transactional
    public CustomerOrder updateStatus(String orderId, UpdateOrderStatusRequest req) {
        CustomerOrder customerOrder = customerOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        customerOrder.setStatus(req.getStatus());
        customerOrderRepository.save(customerOrder);

        // Sync vendor order status
        vendorOrderRepository.findById(orderId).ifPresent(vo -> {
            vo.setStatus(req.getStatus());
            vendorOrderRepository.save(vo);
        });

        return customerOrder;
    }
}
