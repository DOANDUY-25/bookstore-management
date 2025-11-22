package com.bookshop.bookshop.service;

import com.bookshop.bookshop.dto.PaymentDTO;
import com.bookshop.bookshop.entity.Order;
import com.bookshop.bookshop.entity.OrderGift;
import com.bookshop.bookshop.entity.Payment;
import com.bookshop.bookshop.exception.InvalidPaymentException;
import com.bookshop.bookshop.exception.ResourceNotFoundException;
import com.bookshop.bookshop.repository.OrderGiftRepository;
import com.bookshop.bookshop.repository.OrderRepository;
import com.bookshop.bookshop.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PaymentServiceImpl.class);
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderGiftRepository orderGiftRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, OrderRepository orderRepository, 
                             OrderGiftRepository orderGiftRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.orderGiftRepository = orderGiftRepository;
    }
    
    @Override
    public Payment processOrderPayment(Integer orderId, PaymentDTO dto) {
        log.info("Processing payment for order: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setTransactionId(dto.getTransactionId());
        payment.setCreateAt(LocalDateTime.now());
        
        try {
            // Simulate payment processing
            payment.setStatus("COMPLETED");
            payment.setPaidAt(LocalDateTime.now());
            
            // Update order status
            order.setStatus("PAID");
            orderRepository.save(order);
            
            log.info("Payment processed successfully");
        } catch (Exception e) {
            payment.setStatus("FAILED");
            log.error("Payment failed: {}", e.getMessage());
            throw new InvalidPaymentException("Payment processing failed: " + e.getMessage());
        }
        
        return paymentRepository.save(payment);
    }
    
    @Override
    public Payment processGiftOrderPayment(Integer orderGiftId, PaymentDTO dto) {
        log.info("Processing payment for gift order: {}", orderGiftId);
        
        OrderGift orderGift = orderGiftRepository.findById(orderGiftId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderGift", "id", orderGiftId));
        
        Payment payment = new Payment();
        payment.setOrderGift(orderGift);
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setTransactionId(dto.getTransactionId());
        payment.setCreateAt(LocalDateTime.now());
        
        try {
            // Simulate payment processing
            payment.setStatus("COMPLETED");
            payment.setPaidAt(LocalDateTime.now());
            
            // Update gift order
            orderGift.setStatus("PAID");
            orderGift.setPaidAt(LocalDateTime.now());
            orderGift.setPaymentMethod(dto.getPaymentMethod());
            orderGift.setTransactionId(dto.getTransactionId());
            orderGiftRepository.save(orderGift);
            
            log.info("Payment processed successfully");
        } catch (Exception e) {
            payment.setStatus("FAILED");
            log.error("Payment failed: {}", e.getMessage());
            throw new InvalidPaymentException("Payment processing failed: " + e.getMessage());
        }
        
        return paymentRepository.save(payment);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentByOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        return paymentRepository.findByOrder(order)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "orderId", orderId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentByGiftOrder(Integer orderGiftId) {
        OrderGift orderGift = orderGiftRepository.findById(orderGiftId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderGift", "id", orderGiftId));
        return paymentRepository.findByOrderGift(orderGift)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "orderGiftId", orderGiftId));
    }
    
    @Override
    public Payment updatePaymentStatus(Integer paymentId, String status) {
        log.info("Updating payment {} status to {}", paymentId, status);
        
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));
        
        payment.setStatus(status);
        if ("COMPLETED".equals(status) && payment.getPaidAt() == null) {
            payment.setPaidAt(LocalDateTime.now());
        }
        
        return paymentRepository.save(payment);
    }
}
