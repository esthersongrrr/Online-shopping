package com.esther.paymentservice.payload;

import com.esther.paymentservice.util.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentDto {
    private Long id;
    private UUID orderId;
    private BigDecimal amount;
    private PaymentStatus status;

    // Constructors, Getters, and Setters
    public PaymentDto() {}

    public PaymentDto(UUID orderId, BigDecimal amount, PaymentStatus status) {
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}

