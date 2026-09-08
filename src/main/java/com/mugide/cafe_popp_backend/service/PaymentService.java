package com.mugide.cafe_popp_backend.service;

import com.mugide.cafe_popp_backend.dto.PaymentDto;
import com.mugide.cafe_popp_backend.entity.*;
import com.mugide.cafe_popp_backend.enums.OrderStatus;
import com.mugide.cafe_popp_backend.enums.PaymentMethod;
import com.mugide.cafe_popp_backend.enums.TableStatus;
import com.mugide.cafe_popp_backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrdersRepository ordersRepository;
    private final DiningTableRepository diningTableRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          OrdersRepository ordersRepository,
                          DiningTableRepository diningTableRepository) {
        this.paymentRepository = paymentRepository;
        this.ordersRepository = ordersRepository;
        this.diningTableRepository = diningTableRepository;
    }

    private PaymentDto toDto(Payment payment) {
        return new PaymentDto(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getMethod()
        );
    }
    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional
    public PaymentDto payOrder(Long orderId, PaymentMethod method) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.SERVED) {
            throw new RuntimeException("Only SERVED orders can be paid");
        }

        BigDecimal total = order.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Cannot pay an order with zero total");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(total);
        payment.setMethod(method);
        Payment savedPayment = paymentRepository.save(payment);

        order.setStatus(OrderStatus.PAID);
        ordersRepository.save(order);

        DiningTable table = order.getTable();
        table.setStatus(TableStatus.FREE);
        diningTableRepository.save(table);

        return toDto(savedPayment);
    }
}