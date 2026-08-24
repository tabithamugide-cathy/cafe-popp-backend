package com.mugide.cafe_popp_backend.dto;

import java.math.BigDecimal;

public record MenuItemDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        boolean available,
        Long categoryId,
        String categoryName
) {}