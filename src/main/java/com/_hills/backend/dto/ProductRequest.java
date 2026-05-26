package com._hills.backend.dto;

import com._hills.backend.enums.ProductCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    @NotBlank private String name;
    @NotNull private ProductCategory category;
    @NotNull @Min(0) private BigDecimal price;
    @NotNull @Min(0) private Integer stock;
}
