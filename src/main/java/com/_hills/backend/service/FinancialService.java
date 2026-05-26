package com._hills.backend.service;

import com._hills.backend.dto.FinancialSummaryResponse;
import com._hills.backend.enums.OrderStatus;
import com._hills.backend.enums.ProfileStatus;
import com._hills.backend.enums.Role;
import com._hills.backend.repository.CustomerOrderRepository;
import com._hills.backend.repository.ExpenseRepository;
import com._hills.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class FinancialService {

    private final CustomerOrderRepository orderRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public FinancialSummaryResponse getSummary() {
        BigDecimal revenue  = orderRepository.sumDeliveredRevenue();
        BigDecimal expenses = expenseRepository.sumAllExpenses();
        BigDecimal profit   = revenue.subtract(expenses);

        long total     = orderRepository.count();
        long pending   = orderRepository.countByStatus(OrderStatus.PENDING);
        long delivered = orderRepository.countByStatus(OrderStatus.DELIVERED);
        long vendors   = userRepository.findByRoleAndProfileStatus(Role.VENDOR, ProfileStatus.APPROVED).size();

        double rate = total > 0
                ? BigDecimal.valueOf(delivered).divide(BigDecimal.valueOf(total), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)).doubleValue()
                : 0.0;

        return FinancialSummaryResponse.builder()
                .totalRevenue(revenue)
                .totalExpenses(expenses)
                .netProfit(profit)
                .totalOrders(total)
                .pendingOrders(pending)
                .deliveredOrders(delivered)
                .activeVendors(vendors)
                .fulfillmentRate(rate)
                .build();
    }
}
