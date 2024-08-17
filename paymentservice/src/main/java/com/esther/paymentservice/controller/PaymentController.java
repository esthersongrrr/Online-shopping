package com.esther.paymentservice.controller;

import com.esther.paymentservice.payload.PaymentDto;
import com.esther.paymentservice.service.PaymentService;
import com.esther.paymentservice.util.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDto> submitPayment(@RequestBody PaymentDto paymentDto) {
        paymentService.submitPayment(paymentDto);
        return ResponseEntity.ok(paymentDto);
    }

    @PatchMapping("/{id}/refund")
    public ResponseEntity<PaymentDto> reversePayment(@PathVariable Long id) {
        PaymentDto refundedPayment = paymentService.reversePayment(id);
        return ResponseEntity.ok(refundedPayment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPayment(@PathVariable Long id) {
        PaymentDto payment = paymentService.getPayment(id);
        return ResponseEntity.ok(payment);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PaymentDto> updatePaymentStatus(@PathVariable Long id, @RequestBody PaymentStatus status) {
        PaymentDto updatedPayment = paymentService.updatePaymentStatus(id, status);
        return ResponseEntity.ok(updatedPayment);
    }
}

