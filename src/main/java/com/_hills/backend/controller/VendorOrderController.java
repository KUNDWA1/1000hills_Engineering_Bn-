package com._hills.backend.controller;

import com._hills.backend.entity.VendorOrder;
import com._hills.backend.service.VendorOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/orders")
@RequiredArgsConstructor
public class VendorOrderController {

    private final VendorOrderService vendorOrderService;

    @GetMapping
    public ResponseEntity<List<VendorOrder>> getAll(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(vendorOrderService.getMyOrders(user.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorOrder> getOne(@PathVariable String id,
                                               @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(vendorOrderService.getOrderById(id, user.getUsername()));
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<VendorOrder> accept(@PathVariable String id,
                                               @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(vendorOrderService.accept(id, user.getUsername()));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<VendorOrder> reject(@PathVariable String id,
                                               @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(vendorOrderService.reject(id, user.getUsername()));
    }
}
