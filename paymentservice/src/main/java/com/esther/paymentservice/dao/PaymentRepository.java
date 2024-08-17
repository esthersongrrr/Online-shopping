package com.esther.paymentservice.dao;

import com.esther.paymentservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(UUID orderId);
    void deleteByOrderId(UUID orderId);
}