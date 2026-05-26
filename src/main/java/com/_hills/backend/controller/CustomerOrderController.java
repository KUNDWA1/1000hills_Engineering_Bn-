package com._hills.backend.controller;

import com._hills.backend.dto.CustomerOrderResponse;
import com._hills.backend.dto.PlaceOrderRequest;
import com._hills.backend.entity.CustomerOrder;
import com._hills.backend.service.CustomerOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class CustomerOrderController {

    private final CustomerOrderService orderService;

    @PostMapping
    public ResponseEntity<CustomerOrderResponse> place(@AuthenticationPrincipal UserDetails user,
                                                @Valid @RequestBody PlaceOrderRequest req) {
        return ResponseEntity.ok(CustomerOrderResponse.from(orderService.placeOrder(user.getUsername(), req)));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<CustomerOrderResponse>> mine(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(orderService.getMyOrders(user.getUsername()).stream()
                .map(CustomerOrderResponse::from).collect(Collectors.toList()));
    }
}
