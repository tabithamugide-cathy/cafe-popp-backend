package com.mugide.cafe_popp_backend.controller;

import com.mugide.cafe_popp_backend.dto.PaymentDto;
import com.mugide.cafe_popp_backend.enums.PaymentMethod;
import com.mugide.cafe_popp_backend.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentDto payOrder(@RequestParam Long orderId, @RequestParam PaymentMethod method) {
        return paymentService.payOrder(orderId, method);
    }

    @GetMapping
    public List<PaymentDto> getAllPayments() {
        return paymentService.getAllPayments();
    }
}