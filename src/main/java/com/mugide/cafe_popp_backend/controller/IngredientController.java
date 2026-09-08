package com.mugide.cafe_popp_backend.controller;

import com.mugide.cafe_popp_backend.entity.Ingredient;
import com.mugide.cafe_popp_backend.service.IngredientService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public List<Ingredient> getAll() {
        return ingredientService.getAll();
    }

    @GetMapping("/{id}")
    public Ingredient getById(@PathVariable Long id) {
        return ingredientService.getById(id);
    }

    @PostMapping
    public Ingredient create(@RequestBody Ingredient ingredient) {
        return ingredientService.create(ingredient);
    }

    @PatchMapping("/{id}/restock")
    public Ingredient restock(@PathVariable Long id, @RequestParam BigDecimal amount) {
        return ingredientService.restock(id, amount);
    }
}