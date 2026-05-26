package com._hills.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data @Builder
public class FinancialSummaryResponse {
    private BigDecimal totalRevenue;
    private BigDecimal totalExpenses;
    private BigDecimal netProfit;
    private long totalOrders;
    private long pendingOrders;
    private long deliveredOrders;
    private long activeVendors;
    private double fulfillmentRate;
}
