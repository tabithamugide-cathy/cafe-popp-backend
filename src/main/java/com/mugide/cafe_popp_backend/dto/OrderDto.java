package com.mugide.cafe_popp_backend.dto;

import com.mugide.cafe_popp_backend.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderDto(
        Long id,
        Long tableId,
        Integer tableNumber,
        Long staffId,
        String staffName,
        OrderStatus status,
        List<OrderItemDto> items,
        BigDecimal total
) {}