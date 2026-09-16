package com.mugide.cafe_popp_backend.repository;

import com.mugide.cafe_popp_backend.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    @Query("SELECT m FROM MenuItem m JOIN FETCH m.category ORDER BY m.name")
    List<MenuItem> findAll();

    @Query("SELECT m FROM MenuItem m JOIN FETCH m.category WHERE m.category.id = :categoryId ORDER BY m.name")
    List<MenuItem> findByCategoryId(Long categoryId);

    @Query("SELECT m FROM MenuItem m JOIN FETCH m.category WHERE m.available = true ORDER BY m.name")
    List<MenuItem> findByAvailableTrue();
}