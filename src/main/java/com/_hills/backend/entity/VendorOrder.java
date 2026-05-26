package com._hills.backend.entity;

import com._hills.backend.enums.OrderStatus;
import com._hills.backend.enums.VendorStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendor_orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VendorOrder {

    @Id
    private String id; // same as CustomerOrder.id

    @OneToOne
    @JoinColumn(name = "customer_order_id", nullable = false)
    private CustomerOrder customerOrder;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    @JsonIgnoreProperties({"password", "authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled"})
    private User vendor;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.ASSIGNED;

    @Enumerated(EnumType.STRING)
    @Column(name = "vendor_status", nullable = false)
    @Builder.Default
    private VendorStatus vendorStatus = VendorStatus.PENDING;

    @Column(name = "assigned_at", updatable = false)
    @Builder.Default
    private LocalDateTime assignedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "vendorOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnoreProperties("vendorOrder")
    private List<VendorOrderItem> items = new ArrayList<>();
}
