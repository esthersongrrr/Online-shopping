package com.esther.paymentservice.entity;

import com.esther.paymentservice.util.PaymentStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID orderId;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;  // Examples: PENDING, COMPLETED, REFUNDED
    private String transactionId;  // Unique transaction ID to ensure idempotency

    private Date time; // assume the time of payment for order refund.

    public Payment(UUID orderId, PaymentStatus status, BigDecimal amount) {
        this.orderId = orderId;
        this.status = status;
        this.amount = amount;
    }

    public Payment(Long id, UUID orderId, BigDecimal amount, PaymentStatus status, String transactionId) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.transactionId = transactionId;
    }

    public Payment() {

    }

    // Getters and setters
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}

