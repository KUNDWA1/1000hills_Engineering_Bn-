package com._hills.backend.repository;

import com._hills.backend.entity.CustomerOrder;
import com._hills.backend.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, String> {
    List<CustomerOrder> findByCustomerEmail(String email);
    List<CustomerOrder> findByStatus(OrderStatus status);
    List<CustomerOrder> findByVendorId(Long vendorId);

    @Query("SELECT COALESCE(SUM(o.total), 0) FROM CustomerOrder o WHERE o.status = 'DELIVERED'")
    BigDecimal sumDeliveredRevenue();

    long countByStatus(OrderStatus status);
}
