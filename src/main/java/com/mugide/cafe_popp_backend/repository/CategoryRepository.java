package com.mugide.cafe_popp_backend.repository;

import com.mugide.cafe_popp_backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}