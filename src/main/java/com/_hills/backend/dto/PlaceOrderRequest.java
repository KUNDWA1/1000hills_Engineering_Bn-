package com._hills.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PlaceOrderRequest {

    @NotEmpty
    private List<OrderItemDto> items;

    @NotNull @Min(0)
    private BigDecimal total;

    @Data
    public static class OrderItemDto {
        @NotBlank private String productName;
        private String brand;
        @NotNull @Min(1) private Integer qty;
        @NotNull @Min(0) private BigDecimal unitPrice;
    }
}
