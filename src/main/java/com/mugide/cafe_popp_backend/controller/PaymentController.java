package com.mugide.cafe_popp_backend.controller;

import com.mugide.cafe_popp_backend.dto.PaymentDto;
import com.mugide.cafe_popp_backend.enums.PaymentMethod;
import com.mugide.cafe_popp_backend.service.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER', 'WAITER')")
    public List<PaymentDto> getPayments() {
        return paymentService.findAll();
    }

    @PostMapping
    public PaymentDto collect(@RequestParam Long orderId, @RequestParam PaymentMethod method) {
        return paymentService.collect(orderId, method);
    }
}