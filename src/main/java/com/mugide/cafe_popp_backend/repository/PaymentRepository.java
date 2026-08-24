package com.mugide.cafe_popp_backend.repository;

import com.mugide.cafe_popp_backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}