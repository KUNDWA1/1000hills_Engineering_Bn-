package com._hills.backend.controller;

import com._hills.backend.dto.*;
import com._hills.backend.entity.*;
import com._hills.backend.enums.ProfileStatus;
import com._hills.backend.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final VendorProfileService vendorProfileService;
    private final AdminOrderService adminOrderService;
    private final ExpenseService expenseService;
    private final FinancialService financialService;

    // ── VENDOR MANAGEMENT ──────────────────────────────────

    @GetMapping("/vendors")
    public ResponseEntity<List<VendorProfile>> getAllVendors() {
        return ResponseEntity.ok(vendorProfileService.getAllVendorProfiles());
    }

    @GetMapping("/vendors/pending")
    public ResponseEntity<List<VendorProfile>> getPendingVendors() {
        return ResponseEntity.ok(vendorProfileService.getPendingProfiles());
    }

    @GetMapping("/vendors/{id}")
    public ResponseEntity<VendorProfile> getVendor(@PathVariable Long id) {
        return ResponseEntity.ok(vendorProfileService.getProfileById(id));
    }

    @PatchMapping("/vendors/{id}/approve")
    public ResponseEntity<Map<String, String>> approve(@PathVariable Long id,
                                                        @AuthenticationPrincipal UserDetails admin) {
        vendorProfileService.updateStatus(id, ProfileStatus.APPROVED, admin.getUsername());
        return ResponseEntity.ok(Map.of("message", "Vendor approved"));
    }

    @PatchMapping("/vendors/{id}/reject")
    public ResponseEntity<Map<String, String>> reject(@PathVariable Long id,
                                                       @AuthenticationPrincipal UserDetails admin) {
        vendorProfileService.updateStatus(id, ProfileStatus.REJECTED, admin.getUsername());
        return ResponseEntity.ok(Map.of("message", "Vendor rejected"));
    }

    @PatchMapping("/vendors/{id}/suspend")
    public ResponseEntity<Map<String, String>> suspend(@PathVariable Long id,
                                                        @AuthenticationPrincipal UserDetails admin) {
        vendorProfileService.updateStatus(id, ProfileStatus.SUSPENDED, admin.getUsername());
        return ResponseEntity.ok(Map.of("message", "Vendor suspended"));
    }

    // ── ORDER MANAGEMENT ───────────────────────────────────

    @GetMapping("/orders")
    public ResponseEntity<List<CustomerOrderResponse>> getAllOrders() {
        return ResponseEntity.ok(adminOrderService.getAllOrders().stream()
                .map(CustomerOrderResponse::from).collect(Collectors.toList()));
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<CustomerOrderResponse> getOrder(@PathVariable String id) {
        return ResponseEntity.ok(CustomerOrderResponse.from(adminOrderService.getOrderById(id)));
    }

    @PatchMapping("/orders/{id}/assign")
    public ResponseEntity<CustomerOrderResponse> assignOrder(@PathVariable String id,
                                                      @Valid @RequestBody AssignOrderRequest req) {
        return ResponseEntity.ok(CustomerOrderResponse.from(adminOrderService.assignOrder(id, req)));
    }

    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<CustomerOrderResponse> updateStatus(@PathVariable String id,
                                                       @Valid @RequestBody UpdateOrderStatusRequest req) {
        return ResponseEntity.ok(CustomerOrderResponse.from(adminOrderService.updateStatus(id, req)));
    }

    // ── EXPENSES ───────────────────────────────────────────

    @GetMapping("/expenses")
    public ResponseEntity<List<Expense>> getExpenses() {
        return ResponseEntity.ok(expenseService.getAll());
    }

    @PostMapping("/expenses")
    public ResponseEntity<Expense> addExpense(@AuthenticationPrincipal UserDetails admin,
                                               @Valid @RequestBody ExpenseRequest req) {
        return ResponseEntity.ok(expenseService.add(admin.getUsername(), req));
    }

    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<Map<String, String>> deleteExpense(@PathVariable Long id) {
        expenseService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Expense deleted"));
    }

    // ── FINANCIALS ─────────────────────────────────────────

    @GetMapping("/financials")
    public ResponseEntity<FinancialSummaryResponse> getFinancials() {
        return ResponseEntity.ok(financialService.getSummary());
    }
}
