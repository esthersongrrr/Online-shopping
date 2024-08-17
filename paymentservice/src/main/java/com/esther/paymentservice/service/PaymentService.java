package com.esther.paymentservice.service;

import com.esther.paymentservice.dao.PaymentRepository;
import com.esther.paymentservice.entity.Payment;
import com.esther.paymentservice.payload.OrderDto;
import com.esther.paymentservice.payload.PaymentDto;
import com.esther.paymentservice.payload.PaymentMapper;
import com.esther.paymentservice.util.PaymentStatus;
import com.google.gson.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PaymentService {
    private PaymentRepository paymentRepository;

//    GsonBuilder builder = new GsonBuilder();
    private Gson gson; // Gson instance for JSON processing
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // could lead to uneven load distribution across partitions

    public PaymentService(PaymentRepository paymentRepository, Gson gson) {
        this.paymentRepository = paymentRepository;
//        this.gson = new GsonBuilder()
//                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
//                    @Override
//                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
//                            throws JsonParseException {
//                        return new Date(json.getAsJsonPrimitive().getAsLong());
//                    }
//                })
//                .create();
        this.gson = gson;
    }

    @KafkaListener(topics = "order-placed")
    public void handleOrderCreated(String json) {
        // Deserialize JSON back into OrderDto
        if (json.startsWith("\"") && json.endsWith("\"")) {
            json = json.substring(1, json.length() - 1);  // Remove the leading and trailing quotes
            json = json.replace("\\\"", "\"");  // Unescape the internal quotes
        }
        System.out.println("Received JSON: " + json); // Log the raw JSON string
        OrderDto orderDto = gson.fromJson(json, OrderDto.class);
        UUID orderId = orderDto.getId();
        System.out.println("Received order creation for order ID: " + orderId);
        processPayment(orderId, orderDto.getAmount());
    }

    private void processPayment(UUID orderId, BigDecimal amount) {
        // Implement payment processing logic based on order details
        Payment payment = new Payment(orderId, PaymentStatus.PENDING, amount);
        paymentRepository.save(payment);
    }


    @KafkaListener(topics = "order-cancelled")
    public void handleOrderCancelled(String json) {
        OrderDto orderDto = gson.fromJson(json, OrderDto.class);
        UUID orderId = orderDto.getId();
        System.out.println("Received order cancellation for order ID: " + orderId);
        cancelPayment(orderId);
    }

    private void cancelPayment(UUID orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found with orderId: " + orderId));
        paymentRepository.delete(payment);
    }

    @KafkaListener(topics = "order-updated")
    public void handleOrderUpdated(String json) {
        OrderDto orderDto = gson.fromJson(json, OrderDto.class);
        UUID orderId = orderDto.getId();
        System.out.println("Received order update for order ID: " + orderId);
        // Implement additional logic as needed using the orderDto object
        updatePayment(orderId, orderDto.getAmount());
    }

    private void updatePayment(UUID orderId, BigDecimal amount) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found with orderId: " + orderId));
        payment.setAmount(amount);
        paymentRepository.save(payment);
    }

    public void submitPayment(PaymentDto paymentDto) throws IllegalStateException {
        // Check if a payment with the same transactionId exists
        Payment payment = paymentRepository.findByOrderId(paymentDto.getOrderId())
                .orElseThrow(() -> new IllegalStateException("No need to pay."));

        if(payment.getStatus()!=PaymentStatus.PENDING) {
            throw new IllegalStateException(("Payment transaction is already processed."));
        }
        payment.setStatus(PaymentStatus.COMPLETED);  // Assuming instant payment for simplicity
        payment.setOrderId(paymentDto.getOrderId());
        payment.setAmount(paymentDto.getAmount());
        Payment savedPayment = paymentRepository.save(payment);

        OrderDto orderdto = new OrderDto();
        orderdto.setId(paymentDto.getOrderId());
        // Serialize OrderDto to JSON
        String orderJson = gson.toJson(orderdto);
        // Publish to Kafka
        kafkaTemplate.send("payment-success", orderdto.getId().toString(), orderJson);
    }

    public PaymentDto reversePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Payment not found"));

        if (payment.getStatus()!=PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Only completed payments can be refunded.");
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        Payment updatedPayment = paymentRepository.save(payment);

        OrderDto orderdto = new OrderDto();
        orderdto.setId(payment.getOrderId());
        // Serialize OrderDto to JSON
        String orderJson = gson.toJson(orderdto);

        // Publish to Kafka
        kafkaTemplate.send("payment-refunded", orderdto.getId().toString(), orderJson);
        return PaymentMapper.toDto(updatedPayment);
    }

    public PaymentDto updatePaymentStatus(Long id, PaymentStatus status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Payment not found"));
        payment.setStatus(status);
        return PaymentMapper.toDto(paymentRepository.save(payment));
    }

    public PaymentDto getPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Payment not found"));
        return PaymentMapper.toDto(paymentRepository.save(payment));
    }
}

