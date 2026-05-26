package com._hills.backend.service;

import com._hills.backend.dto.ProductRequest;
import com._hills.backend.entity.User;
import com._hills.backend.entity.VendorProduct;
import com._hills.backend.repository.UserRepository;
import com._hills.backend.repository.VendorProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorProductService {

    private final VendorProductRepository productRepository;
    private final UserRepository userRepository;

    public List<VendorProduct> getMyProducts(String email) {
        return productRepository.findByVendorEmail(email);
    }

    public VendorProduct addProduct(String email, ProductRequest req) {
        User vendor = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        VendorProduct product = VendorProduct.builder()
                .vendor(vendor)
                .name(req.getName())
                .category(req.getCategory())
                .price(req.getPrice())
                .stock(req.getStock())
                .build();

        return productRepository.save(product);
    }

    public VendorProduct updateProduct(Long id, String email, ProductRequest req) {
        VendorProduct product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getVendor().getEmail().equals(email))
            throw new RuntimeException("Unauthorized");

        product.setName(req.getName());
        product.setCategory(req.getCategory());
        product.setPrice(req.getPrice());
        product.setStock(req.getStock());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id, String email) {
        VendorProduct product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getVendor().getEmail().equals(email))
            throw new RuntimeException("Unauthorized");

        productRepository.delete(product);
    }
}
