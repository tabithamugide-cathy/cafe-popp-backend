package com.mugide.cafe_popp_backend.controller;

import com.mugide.cafe_popp_backend.dto.RecipeDto;
import com.mugide.cafe_popp_backend.service.RecipeService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/menu-item/{menuItemId}")
    public List<RecipeDto> getRecipe(@PathVariable Long menuItemId) {
        return recipeService.getRecipeForMenuItem(menuItemId);
    }

    @PostMapping
    public RecipeDto addRecipeLine(@RequestParam Long menuItemId,
                                   @RequestParam Long ingredientId,
                                   @RequestParam BigDecimal quantityRequired) {
        return recipeService.addRecipeLine(menuItemId, ingredientId, quantityRequired);
    }
}