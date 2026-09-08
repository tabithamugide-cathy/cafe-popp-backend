package com.mugide.cafe_popp_backend.service;

import com.mugide.cafe_popp_backend.dto.DashboardDto;
import com.mugide.cafe_popp_backend.entity.Orders;
import com.mugide.cafe_popp_backend.enums.OrderStatus;
import com.mugide.cafe_popp_backend.repository.DiningTableRepository;
import com.mugide.cafe_popp_backend.repository.IngredientRepository;
import com.mugide.cafe_popp_backend.repository.MenuItemRepository;
import com.mugide.cafe_popp_backend.repository.OrderItemRepository;
import com.mugide.cafe_popp_backend.repository.OrdersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardService {
    private final OrdersRepository ordersRepository;
    private final OrderItemRepository orderItemRepository;
    private final MenuItemRepository menuItemRepository;
    private final DiningTableRepository diningTableRepository;

    public DashboardService(OrdersRepository ordersRepository,
                            OrderItemRepository orderItemRepository,
                            MenuItemRepository menuItemRepository,
                            DiningTableRepository diningTableRepository) {
        this.ordersRepository = ordersRepository;
        this.orderItemRepository = orderItemRepository;
        this.menuItemRepository = menuItemRepository;
        this.diningTableRepository = diningTableRepository;
    }

    @Transactional(readOnly = true)
    public DashboardDto getSummary() {
        List<Orders> recent = ordersRepository.findTop5ByOrderByIdDesc();
        List<DashboardDto.DashboardOrderDto> recentOrders = recent.stream()
                .map(order -> new DashboardDto.DashboardOrderDto(
                        order.getId(), order.getTable().getTableNumber(),
                        order.getStaff().getFullName(), order.getStatus().name(), total(order)))
                .toList();

        List<DashboardDto.TopProductDto> topProducts = orderItemRepository.findTopSellingProducts()
                .stream().limit(5)
                .map(row -> new DashboardDto.TopProductDto(
                        (String) row[0], ((Number) row[1]).longValue(), (BigDecimal) row[2]))
                .toList();

        return new DashboardDto(
                ordersRepository.count(),
                ordersRepository.countByStatus(OrderStatus.OPEN)
                        + ordersRepository.countByStatus(OrderStatus.IN_PROGRESS),
                menuItemRepository.count(),
                diningTableRepository.countByStatus(com.mugide.cafe_popp_backend.enums.TableStatus.FREE),
                orderItemRepository.calculateRevenue(), recentOrders, topProducts);
    }

    private BigDecimal total(Orders order) {
        return order.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}