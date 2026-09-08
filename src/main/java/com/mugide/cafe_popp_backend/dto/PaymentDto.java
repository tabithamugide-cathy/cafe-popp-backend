package com.mugide.cafe_popp_backend.dto;

import com.mugide.cafe_popp_backend.enums.PaymentMethod;

import java.math.BigDecimal;

public record PaymentDto(
        Long id,
        Long orderId,
        BigDecimal amount,
        PaymentMethod method
) {}