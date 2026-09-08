package com.mugide.cafe_popp_backend.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardDto(
        long totalOrders,
        long pendingOrders,
        long menuItems,
        long availableTables,
        BigDecimal totalRevenue,
        List<DashboardOrderDto> recentOrders,
        List<TopProductDto> topProducts
) {
    public record DashboardOrderDto(Long id, Integer tableNumber, String staffName, String status, BigDecimal total) {}
    public record TopProductDto(String name, long quantity, BigDecimal revenue) {}
}