package com.mugide.cafe_popp_backend.service;

import com.mugide.cafe_popp_backend.dto.PaymentDto;
import com.mugide.cafe_popp_backend.entity.Orders;
import com.mugide.cafe_popp_backend.entity.Payment;
import com.mugide.cafe_popp_backend.enums.OrderStatus;
import com.mugide.cafe_popp_backend.enums.PaymentMethod;
import com.mugide.cafe_popp_backend.repository.OrdersRepository;
import com.mugide.cafe_popp_backend.repository.PaymentRepository;
import com.mugide.cafe_popp_backend.repository.DiningTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrdersRepository ordersRepository;
    private final DiningTableRepository diningTableRepository;

    public PaymentService(PaymentRepository paymentRepository, OrdersRepository ordersRepository,
                          DiningTableRepository diningTableRepository) {
        this.paymentRepository = paymentRepository;
        this.ordersRepository = ordersRepository;
        this.diningTableRepository = diningTableRepository;
    }

    @Transactional
    public PaymentDto collect(Long orderId, PaymentMethod method) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        if (order.getStatus() != OrderStatus.SERVED) {
            throw new IllegalStateException("Only served orders can be paid");
        }
        if (paymentRepository.findByOrderId(orderId).isPresent()) {
            throw new IllegalStateException("Order has already been paid");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())))
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add));
        payment.setMethod(method);
        order.setStatus(OrderStatus.PAID);
        order.getTable().setStatus(com.mugide.cafe_popp_backend.enums.TableStatus.FREE);
        diningTableRepository.save(order.getTable());
        return toDto(paymentRepository.save(payment));
    }

    @Transactional(readOnly = true)
    public List<PaymentDto> findAll() {
        return paymentRepository.findAll().stream().map(this::toDto).toList();
    }

    private PaymentDto toDto(Payment payment) {
        return new PaymentDto(payment.getId(), payment.getOrder().getId(), payment.getAmount(), payment.getMethod());
    }
}
