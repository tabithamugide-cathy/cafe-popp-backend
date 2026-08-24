package com.mugide.cafe_popp_backend.repository;

import com.mugide.cafe_popp_backend.entity.Orders;
import com.mugide.cafe_popp_backend.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByStatus(OrderStatus status);
    List<Orders> findByTableId(Long tableId);
}