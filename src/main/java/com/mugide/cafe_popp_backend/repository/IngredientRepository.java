package com.mugide.cafe_popp_backend.repository;

import com.mugide.cafe_popp_backend.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}