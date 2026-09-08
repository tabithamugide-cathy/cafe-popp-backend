package com.mugide.cafe_popp_backend.controller;

import com.mugide.cafe_popp_backend.dto.OrderDto;
import com.mugide.cafe_popp_backend.enums.OrderStatus;
import com.mugide.cafe_popp_backend.service.OrderService;
import org.springframework.web.bind.annotation.*;

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
    public OrderDto createOrder(@RequestParam Long tableId, @RequestParam Long staffId) {
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

    @PatchMapping("/{orderId}/status")
    public OrderDto updateStatus(@PathVariable Long orderId, @RequestParam OrderStatus newStatus) {
        return orderService.updateStatus(orderId, newStatus);
    }

    @GetMapping
    public List<OrderDto> getOrdersByStatus(@RequestParam OrderStatus status) {
        return orderService.getOrdersByStatus(status);
    }

    @DeleteMapping("/{orderId}/items/{orderItemId}")
    public OrderDto removeItem(@PathVariable Long orderId, @PathVariable Long orderItemId) {
        return orderService.removeItemFromOrder(orderId, orderItemId);
    }

    @PatchMapping("/{orderId}/items/{orderItemId}")
    public OrderDto updateItemQuantity(@PathVariable Long orderId,
                                       @PathVariable Long orderItemId,
                                       @RequestParam int quantity) {
        return orderService.updateItemQuantity(orderId, orderItemId, quantity);
    }

    @PostMapping("/{orderId}/serve")
    public OrderDto markServed(@PathVariable Long orderId) {
        return orderService.markServed(orderId);
    }
    

    @GetMapping("/{orderId}")
    public OrderDto getOrder(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/all")
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }
}