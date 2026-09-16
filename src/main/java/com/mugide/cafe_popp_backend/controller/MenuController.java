package com.mugide.cafe_popp_backend.controller;

import com.mugide.cafe_popp_backend.dto.MenuItemDto;
import com.mugide.cafe_popp_backend.entity.Category;
import com.mugide.cafe_popp_backend.entity.MenuItem;
import com.mugide.cafe_popp_backend.service.MenuService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/items")
    public List<MenuItemDto> getAllItems() {
        return menuService.getAllMenuItems();
    }

    @GetMapping("/items/available")
    public List<MenuItemDto> getAvailableItems() {
        return menuService.getAvailableMenuItems();
    }

    @GetMapping("/items/{id}")
    public MenuItemDto getItem(@PathVariable Long id) {
        return menuService.getMenuItemById(id);
    }

    @PostMapping("/items")
    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    public MenuItemDto createItem(@RequestBody MenuItem menuItem) {
        return menuService.createMenuItem(menuItem);
    }

    @PatchMapping("/items/{id}/availability")
    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    public MenuItemDto updateAvailability(@PathVariable Long id, @RequestParam boolean available) {
        return menuService.updateAvailability(id, available);
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return menuService.getAllCategories();
    }

    @PostMapping("/categories")
    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    public Category createCategory(@RequestBody Category category) {
        return menuService.createCategory(category);
    }
}