package com.mugide.cafe_popp_backend.repository;

import com.mugide.cafe_popp_backend.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	@Query("SELECT COALESCE(SUM(i.unitPrice * i.quantity), 0) FROM OrderItem i")
	BigDecimal calculateRevenue();

	@Query("SELECT i.menuItem.name, SUM(i.quantity), SUM(i.unitPrice * i.quantity) " +
			"FROM OrderItem i GROUP BY i.menuItem.name ORDER BY SUM(i.quantity) DESC")
	List<Object[]> findTopSellingProducts();
}