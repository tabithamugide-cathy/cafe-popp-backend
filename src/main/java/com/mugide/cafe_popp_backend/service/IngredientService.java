package com.mugide.cafe_popp_backend.service;

import com.mugide.cafe_popp_backend.entity.Ingredient;
import com.mugide.cafe_popp_backend.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> getAll() {
        return ingredientRepository.findAll();
    }

    public Ingredient getById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found: " + id));
    }

    public Ingredient create(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public Ingredient restock(Long id, java.math.BigDecimal amount) {
        Ingredient ingredient = getById(id);
        ingredient.setQuantityInStock(ingredient.getQuantityInStock().add(amount));
        return ingredientRepository.save(ingredient);
    }
}