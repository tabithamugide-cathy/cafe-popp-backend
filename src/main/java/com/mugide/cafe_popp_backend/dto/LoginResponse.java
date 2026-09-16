package com.mugide.cafe_popp_backend.dto;

import com.mugide.cafe_popp_backend.enums.UserRole;

public record LoginResponse(
        String token,
        Long userId,
        String fullName,
        String email,
        UserRole role
) {}
