package com._hills.backend.controller;

import com._hills.backend.dto.ProductRequest;
import com._hills.backend.entity.VendorProduct;
import com._hills.backend.service.VendorProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vendor/products")
@RequiredArgsConstructor
public class VendorProductController {

    private final VendorProductService productService;

    @GetMapping
    public ResponseEntity<List<VendorProduct>> getAll(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(productService.getMyProducts(user.getUsername()));
    }

    @PostMapping
    public ResponseEntity<VendorProduct> add(@AuthenticationPrincipal UserDetails user,
                                              @Valid @RequestBody ProductRequest req) {
        return ResponseEntity.ok(productService.addProduct(user.getUsername(), req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendorProduct> update(@PathVariable Long id,
                                                 @AuthenticationPrincipal UserDetails user,
                                                 @Valid @RequestBody ProductRequest req) {
        return ResponseEntity.ok(productService.updateProduct(id, user.getUsername(), req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id,
                                                       @AuthenticationPrincipal UserDetails user) {
        productService.deleteProduct(id, user.getUsername());
        return ResponseEntity.ok(Map.of("message", "Product deleted"));
    }
}
