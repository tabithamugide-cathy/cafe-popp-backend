package com.mugide.cafe_popp_backend.dto;

import java.math.BigDecimal;

public record OrderItemDto(
        Long id,
        Long menuItemId,
        String menuItemName,
        int quantity,
        BigDecimal unitPrice,
        String notes
) {}