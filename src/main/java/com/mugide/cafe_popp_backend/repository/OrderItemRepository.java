package com.mugide.cafe_popp_backend.repository;

import com.mugide.cafe_popp_backend.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}