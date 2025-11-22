package com.bookshop.bookshop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentId")
    private Integer paymentId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderGiftId")
    private OrderGift orderGift;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private Order order;
    
    @Size(max = 100, message = "Transaction ID must not exceed 100 characters")
    @Column(name = "transactionId", length = 100)
    private String transactionId;
    
    @NotBlank(message = "Payment method is required")
    @Size(max = 50, message = "Payment method must not exceed 50 characters")
    @Column(name = "paymentMethod", length = 50)
    private String paymentMethod;
    
    @Column(name = "status", length = 50)
    private String status;
    
    @Column(name = "createAt")
    private LocalDateTime createAt;
    
    @Column(name = "paidAt")
    private LocalDateTime paidAt;
    
    @PrePersist
    protected void onCreate() {
        if (createAt == null) {
            createAt = LocalDateTime.now();
        }
        if (status == null) {
            status = "PENDING";
        }
    }

    // Constructors
    public Payment() {}

    public Payment(Integer paymentId, OrderGift orderGift, Order order, String transactionId, 
                   String paymentMethod, String status, LocalDateTime createAt, LocalDateTime paidAt) {
        this.paymentId = paymentId;
        this.orderGift = orderGift;
        this.order = order;
        this.transactionId = transactionId;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.createAt = createAt;
        this.paidAt = paidAt;
    }

    // Getters and Setters
    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public OrderGift getOrderGift() {
        return orderGift;
    }

    public void setOrderGift(OrderGift orderGift) {
        this.orderGift = orderGift;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}
