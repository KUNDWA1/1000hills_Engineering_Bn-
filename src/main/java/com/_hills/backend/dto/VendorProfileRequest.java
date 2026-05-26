package com._hills.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VendorProfileRequest {
    @NotBlank private String companyName;
    @NotBlank private String contactPerson;
    @NotBlank private String email;
    @NotBlank private String phone;
    @NotBlank private String location;
    @NotBlank private String registrationNo;
    private String description;
}
