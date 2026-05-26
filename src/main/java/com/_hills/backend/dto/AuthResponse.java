package com._hills.backend.dto;

import com._hills.backend.enums.ProfileStatus;
import com._hills.backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data @Builder @AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long id;
    private String name;
    private String email;
    private Role role;
    private ProfileStatus profileStatus;
}
