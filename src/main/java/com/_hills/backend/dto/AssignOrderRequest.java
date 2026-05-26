package com._hills.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class AssignOrderRequest {

    @NotNull
    private Long vendorId;

    @NotNull
    private LocalDate deliveryDate;

    private String description;

    @NotEmpty
    private List<VendorItemDto> products;

    @Data
    public static class VendorItemDto {
        @NotBlank private String productName;
        @NotNull @Min(1) private Integer qty;
        @NotNull @Min(0) private BigDecimal unitPrice;
    }
}
