package com.mugide.cafe_popp_backend.repository;

import com.mugide.cafe_popp_backend.entity.MenuItemIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuItemIngredientRepository extends JpaRepository<MenuItemIngredient, Long> {

    @Query("SELECT mi FROM MenuItemIngredient mi JOIN FETCH mi.ingredient JOIN FETCH mi.menuItem WHERE mi.menuItem.id = :menuItemId")
    List<MenuItemIngredient> findByMenuItemId(Long menuItemId);
}