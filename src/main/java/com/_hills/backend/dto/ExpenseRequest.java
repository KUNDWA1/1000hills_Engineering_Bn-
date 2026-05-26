package com._hills.backend.dto;

import com._hills.backend.enums.ExpenseCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExpenseRequest {
    @NotBlank private String label;
    @NotNull @Min(0) private BigDecimal amount;
    @NotNull private ExpenseCategory category;
}
