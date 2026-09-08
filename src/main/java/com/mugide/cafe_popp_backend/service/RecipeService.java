package com.mugide.cafe_popp_backend.service;

import com.mugide.cafe_popp_backend.dto.RecipeDto;
import com.mugide.cafe_popp_backend.entity.Ingredient;
import com.mugide.cafe_popp_backend.entity.MenuItem;
import com.mugide.cafe_popp_backend.entity.MenuItemIngredient;
import com.mugide.cafe_popp_backend.repository.IngredientRepository;
import com.mugide.cafe_popp_backend.repository.MenuItemIngredientRepository;
import com.mugide.cafe_popp_backend.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RecipeService {

    private final MenuItemIngredientRepository menuItemIngredientRepository;
    private final MenuItemRepository menuItemRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeService(MenuItemIngredientRepository menuItemIngredientRepository,
                         MenuItemRepository menuItemRepository,
                         IngredientRepository ingredientRepository) {
        this.menuItemIngredientRepository = menuItemIngredientRepository;
        this.menuItemRepository = menuItemRepository;
        this.ingredientRepository = ingredientRepository;
    }

    private RecipeDto toDto(MenuItemIngredient line) {
        return new RecipeDto(
                line.getId(),
                line.getMenuItem().getId(),
                line.getMenuItem().getName(),
                line.getIngredient().getId(),
                line.getIngredient().getName(),
                line.getIngredient().getUnit().name(),
                line.getQuantityRequired()
        );
    }

    public List<RecipeDto> getRecipeForMenuItem(Long menuItemId) {
        return menuItemIngredientRepository.findByMenuItemId(menuItemId)
                .stream().map(this::toDto).toList();
    }

    public RecipeDto addRecipeLine(Long menuItemId, Long ingredientId, BigDecimal quantityRequired) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Menu item not found: " + menuItemId));
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found: " + ingredientId));

        MenuItemIngredient line = new MenuItemIngredient();
        line.setMenuItem(menuItem);
        line.setIngredient(ingredient);
        line.setQuantityRequired(quantityRequired);

        return toDto(menuItemIngredientRepository.save(line));
    }
}