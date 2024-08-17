package com.esther.paymentservice.payload;

import com.esther.paymentservice.entity.Payment;

public class PaymentMapper {
    public static PaymentDto toDto(Payment payment) {
        return new PaymentDto(
                payment.getOrderId(),
                payment.getAmount(),
                payment.getStatus()
        );
    }

    public static Payment fromDto(Payment payment, PaymentDto paymentDto) {
        payment.setOrderId(paymentDto.getOrderId());
        payment.setAmount(paymentDto.getAmount());
        payment.setStatus(paymentDto.getStatus());
        return payment;
    }
}

