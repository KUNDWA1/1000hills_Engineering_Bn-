package com._hills.backend.service;

import com._hills.backend.dto.PlaceOrderRequest;
import com._hills.backend.entity.CustomerOrder;
import com._hills.backend.entity.CustomerOrderItem;
import com._hills.backend.entity.User;
import com._hills.backend.repository.CustomerOrderRepository;
import com._hills.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerOrderService {

    private final CustomerOrderRepository orderRepository;
    private final UserRepository userRepository;

    public CustomerOrder placeOrder(String email, PlaceOrderRequest req) {
        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        String orderId = "#ORD-" + System.currentTimeMillis();

        CustomerOrder order = CustomerOrder.builder()
                .id(orderId)
                .customer(customer)
                .total(req.getTotal())
                .build();

        List<CustomerOrderItem> items = req.getItems().stream().map(i ->
                CustomerOrderItem.builder()
                        .order(order)
                        .productName(i.getProductName())
                        .brand(i.getBrand())
                        .qty(i.getQty())
                        .unitPrice(i.getUnitPrice())
                        .build()
        ).toList();

        order.setItems(items);
        return orderRepository.save(order);
    }

    public List<CustomerOrder> getMyOrders(String email) {
        return orderRepository.findByCustomerEmail(email);
    }
}
