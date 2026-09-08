package com.mugide.cafe_popp_backend.service;

import com.mugide.cafe_popp_backend.dto.MenuItemDto;
import com.mugide.cafe_popp_backend.entity.Category;
import com.mugide.cafe_popp_backend.entity.MenuItem;
import com.mugide.cafe_popp_backend.repository.CategoryRepository;
import com.mugide.cafe_popp_backend.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    public MenuService(MenuItemRepository menuItemRepository, CategoryRepository categoryRepository) {
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
    }

    private MenuItemDto toDto(MenuItem item) {
        return new MenuItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.isAvailable(),
                item.getCategory().getId(),
                item.getCategory().getName(),
                item.getImageUrl()
        );
    }

    public List<MenuItemDto> getAllMenuItems() {
        return menuItemRepository.findAll().stream().map(this::toDto).toList();
    }

    public List<MenuItemDto> getAvailableMenuItems() {
        return menuItemRepository.findByAvailableTrue().stream().map(this::toDto).toList();
    }

    public MenuItemDto getMenuItemById(Long id) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found: " + id));
        return toDto(item);
    }

    public MenuItemDto createMenuItem(MenuItem menuItem) {
        return toDto(menuItemRepository.save(menuItem));
    }

    public MenuItemDto updateAvailability(Long id, boolean available) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found: " + id));
        item.setAvailable(available);
        return toDto(menuItemRepository.save(item));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }
}