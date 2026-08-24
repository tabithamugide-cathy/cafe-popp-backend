package com.mugide.cafe_popp_backend.repository;

import com.mugide.cafe_popp_backend.entity.MenuItemIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemIngredientRepository extends JpaRepository<MenuItemIngredient, Long> {
    List<MenuItemIngredient> findByMenuItemId(Long menuItemId);
}