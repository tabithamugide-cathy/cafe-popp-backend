package com.mugide.cafe_popp_backend.controller;

import com.mugide.cafe_popp_backend.dto.OrderDto;
import com.mugide.cafe_popp_backend.enums.OrderStatus;
import com.mugide.cafe_popp_backend.service.OrderService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderDto createOrder(@RequestParam Long tableId,
                                @RequestParam(required = false) Long staffId) {
        return orderService.createOrder(tableId, staffId);
    }

    @PostMapping("/{orderId}/items")
    public OrderDto addItem(@PathVariable Long orderId,
                            @RequestParam Long menuItemId,
                            @RequestParam int quantity,
                            @RequestParam(required = false) String notes) {
        return orderService.addItemToOrder(orderId, menuItemId, quantity, notes);
    }

    @PostMapping("/{orderId}/confirm")
    public OrderDto confirmOrder(@PathVariable Long orderId) {
        return orderService.confirmOrder(orderId);
    }

    @GetMapping("/{orderId}/total")
    public BigDecimal getOrderTotal(@PathVariable Long orderId) {
        return orderService.calculateOrderTotal(orderId);
    }

    @GetMapping("/{orderId}")
    public OrderDto getOrder(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER', 'WAITER', 'KITCHEN')")
    public OrderDto updateStatus(@PathVariable Long orderId, @RequestParam OrderStatus newStatus) {
        return orderService.updateStatus(orderId, newStatus);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER', 'WAITER', 'KITCHEN')")
    public List<OrderDto> getOrdersByStatus(@RequestParam OrderStatus status) {
        return orderService.getOrdersByStatus(status);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER', 'WAITER', 'KITCHEN')")
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }
}