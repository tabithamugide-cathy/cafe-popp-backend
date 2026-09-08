package com.mugide.cafe_popp_backend.service;

import com.mugide.cafe_popp_backend.dto.OrderDto;
import com.mugide.cafe_popp_backend.dto.OrderItemDto;
import com.mugide.cafe_popp_backend.entity.*;
import com.mugide.cafe_popp_backend.enums.OrderStatus;
import com.mugide.cafe_popp_backend.enums.TableStatus;
import com.mugide.cafe_popp_backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final MenuItemRepository menuItemRepository;
    private final DiningTableRepository diningTableRepository;
    private final AppUserRepository appUserRepository;
    private final MenuItemIngredientRepository menuItemIngredientRepository;
    private final IngredientRepository ingredientRepository;

    public OrderService(OrdersRepository ordersRepository,
                        MenuItemRepository menuItemRepository,
                        DiningTableRepository diningTableRepository,
                        AppUserRepository appUserRepository,
                        MenuItemIngredientRepository menuItemIngredientRepository,
                        IngredientRepository ingredientRepository) {
        this.ordersRepository = ordersRepository;
        this.menuItemRepository = menuItemRepository;
        this.diningTableRepository = diningTableRepository;
        this.appUserRepository = appUserRepository;
        this.menuItemIngredientRepository = menuItemIngredientRepository;
        this.ingredientRepository = ingredientRepository;
    }

    private OrderDto toDto(Orders order) {
        BigDecimal total = order.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<OrderItemDto> itemDtos = order.getItems().stream()
                .map(item -> new OrderItemDto(
                        item.getId(),
                        item.getMenuItem().getId(),
                        item.getMenuItem().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getNotes()
                ))
                .toList();

        return new OrderDto(
                order.getId(),
                order.getTable().getId(),
                order.getTable().getTableNumber(),
                order.getStaff().getId(),
                order.getStaff().getFullName(),
                order.getStatus(),
                itemDtos,
                total
        );
    }

    @Transactional
    public OrderDto createOrder(Long tableId, Long staffId) {
        DiningTable table = diningTableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found: " + tableId));
        AppUser staff = appUserRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found: " + staffId));

        Orders order = new Orders();
        order.setTable(table);
        order.setStaff(staff);
        order.setStatus(OrderStatus.OPEN);

        return toDto(ordersRepository.save(order));
    }

    @Transactional
    public OrderDto addItemToOrder(Long orderId, Long menuItemId, int quantity, String notes) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Menu item not found: " + menuItemId));

        if (!menuItem.isAvailable()) {
            throw new RuntimeException("Menu item is not available: " + menuItem.getName());
        }

        OrderItem item = new OrderItem();
        item.setMenuItem(menuItem);
        item.setQuantity(quantity);
        item.setUnitPrice(menuItem.getPrice());
        item.setNotes(notes);

        order.addItem(item);
        return toDto(ordersRepository.save(order));
    }

    @Transactional
    public OrderDto confirmOrder(Long orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.OPEN) {
            throw new RuntimeException("Only OPEN orders can be confirmed");
        }
        if (order.getItems().isEmpty()) {
            throw new RuntimeException("Cannot confirm an order with no items");
        }

        deductStockForOrder(order);

        order.setStatus(OrderStatus.IN_PROGRESS);

        DiningTable table = order.getTable();
        table.setStatus(TableStatus.OCCUPIED);
        diningTableRepository.save(table);

        return toDto(ordersRepository.save(order));
    }

    @Transactional
    public OrderDto markServed(Long orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new RuntimeException("Only IN_PROGRESS orders can be marked as served");
        }

        order.setStatus(OrderStatus.SERVED);
        return toDto(ordersRepository.save(order));
    }
    @Transactional
    public OrderDto removeItemFromOrder(Long orderId, Long orderItemId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.OPEN) {
            throw new RuntimeException("Can only remove items from an OPEN order");
        }

        boolean removed = order.getItems().removeIf(item -> item.getId().equals(orderItemId));
        if (!removed) {
            throw new RuntimeException("Order item not found: " + orderItemId);
        }

        return toDto(ordersRepository.save(order));
    }

    @Transactional
    public OrderDto updateItemQuantity(Long orderId, Long orderItemId, int newQuantity) {
        if (newQuantity <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }

        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.OPEN) {
            throw new RuntimeException("Can only update items on an OPEN order");
        }

        OrderItem item = order.getItems().stream()
                .filter(i -> i.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order item not found: " + orderItemId));

        item.setQuantity(newQuantity);
        return toDto(ordersRepository.save(order));
    }

    private void deductStockForOrder(Orders order) {
        for (OrderItem orderItem : order.getItems()) {
            List<MenuItemIngredient> recipe =
                    menuItemIngredientRepository.findByMenuItemId(orderItem.getMenuItem().getId());

            for (MenuItemIngredient recipeLine : recipe) {
                Ingredient ingredient = recipeLine.getIngredient();
                BigDecimal totalNeeded = recipeLine.getQuantityRequired()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity()));

                if (ingredient.getQuantityInStock().compareTo(totalNeeded) < 0) {
                    throw new RuntimeException(
                            "Insufficient stock for ingredient: " + ingredient.getName());
                }

                ingredient.setQuantityInStock(ingredient.getQuantityInStock().subtract(totalNeeded));
                ingredientRepository.save(ingredient);
            }
        }
    }

    @Transactional
    public BigDecimal calculateOrderTotal(Long orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        return order.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public OrderDto updateStatus(Long orderId, OrderStatus newStatus) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        order.setStatus(newStatus);
        return toDto(ordersRepository.save(order));
    }

    public List<OrderDto> getOrdersByStatus(OrderStatus status) {
        return ordersRepository.findByStatus(status).stream().map(this::toDto).toList();
    }


    @Transactional
    public OrderDto getOrderById(Long orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        return toDto(order);
    }

    @Transactional
    public List<OrderDto> getAllOrders() {
        return ordersRepository.findAll().stream().map(this::toDto).toList();
    }
}