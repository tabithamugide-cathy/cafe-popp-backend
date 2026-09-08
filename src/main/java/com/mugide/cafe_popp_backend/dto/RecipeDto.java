package com.mugide.cafe_popp_backend.dto;

import java.math.BigDecimal;

public record RecipeDto(
        Long id,
        Long menuItemId,
        String menuItemName,
        Long ingredientId,
        String ingredientName,
        String ingredientUnit,
        BigDecimal quantityRequired
) {}