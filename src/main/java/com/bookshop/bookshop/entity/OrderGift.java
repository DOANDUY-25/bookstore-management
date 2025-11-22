package com.bookshop.bookshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordergift")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OrderGift {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderGiftId")
    private Integer orderGiftId;
    
    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    @JsonIgnoreProperties({"orders", "orderGifts", "cart", "membership", "password", "hibernateLazyInitializer", "handler"})
    private User user;
    
    @NotBlank(message = "Recipient name is required")
    @Size(max = 255, message = "Recipient name must not exceed 255 characters")
    @Column(name = "recipientName", length = 255)
    private String recipientName;
    
    @NotBlank(message = "Recipient phone is required")
    @Size(max = 20, message = "Recipient phone must not exceed 20 characters")
    @Column(name = "recipientPhone", length = 20)
    private String recipientPhone;
    
    @NotBlank(message = "Recipient address is required")
    @Size(max = 255, message = "Recipient address must not exceed 255 characters")
    @Column(name = "recipientAddress", length = 255)
    private String recipientAddress;
    
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giftPackageId")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private GiftPackage giftPackage;
    
    @Column(name = "deliveryDate")
    private LocalDateTime deliveryDate;
    
    @Size(max = 100, message = "Delivery method must not exceed 100 characters")
    @Column(name = "deliveryMethod", length = 100)
    private String deliveryMethod;
    
    @DecimalMin(value = "0.0", message = "Gift wrap fee must be at least 0")
    @Digits(integer = 10, fraction = 2, message = "Gift wrap fee must have at most 10 integer digits and 2 decimal places")
    @Column(name = "giftWrapFee", precision = 12, scale = 2)
    private BigDecimal giftWrapFee;
    
    @DecimalMin(value = "0.0", message = "Shipping fee must be at least 0")
    @Digits(integer = 10, fraction = 2, message = "Shipping fee must have at most 10 integer digits and 2 decimal places")
    @Column(name = "shippingFee", precision = 12, scale = 2)
    private BigDecimal shippingFee;
    
    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", message = "Total amount must be at least 0")
    @Digits(integer = 10, fraction = 2, message = "Total amount must have at most 10 integer digits and 2 decimal places")
    @Column(name = "totalAmount", precision = 12, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(name = "status", length = 50)
    private String status;
    
    @Column(name = "createAt")
    private LocalDateTime createAt;
    
    @Column(name = "paidAt")
    private LocalDateTime paidAt;
    
    @Size(max = 50, message = "Payment method must not exceed 50 characters")
    @Column(name = "paymentMethod", length = 50)
    private String paymentMethod;
    
    @Size(max = 100, message = "Transaction ID must not exceed 100 characters")
    @Column(name = "transactionId", length = 100)
    private String transactionId;
    
    @OneToMany(mappedBy = "orderGift", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"orderGift", "hibernateLazyInitializer", "handler"})
    private List<OrderGiftDetail> orderGiftDetails = new ArrayList<>();
    
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
    public OrderGift() {}

    public OrderGift(Integer orderGiftId, User user, String recipientName, String recipientPhone, 
                     String recipientAddress, String message, GiftPackage giftPackage, 
                     LocalDateTime deliveryDate, String deliveryMethod, BigDecimal giftWrapFee, 
                     BigDecimal shippingFee, BigDecimal totalAmount, String status, 
                     LocalDateTime createAt, LocalDateTime paidAt, String paymentMethod, 
                     String transactionId, List<OrderGiftDetail> orderGiftDetails) {
        this.orderGiftId = orderGiftId;
        this.user = user;
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
        this.recipientAddress = recipientAddress;
        this.message = message;
        this.giftPackage = giftPackage;
        this.deliveryDate = deliveryDate;
        this.deliveryMethod = deliveryMethod;
        this.giftWrapFee = giftWrapFee;
        this.shippingFee = shippingFee;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createAt = createAt;
        this.paidAt = paidAt;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.orderGiftDetails = orderGiftDetails;
    }

    // Getters and Setters
    public Integer getOrderGiftId() {
        return orderGiftId;
    }

    public void setOrderGiftId(Integer orderGiftId) {
        this.orderGiftId = orderGiftId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GiftPackage getGiftPackage() {
        return giftPackage;
    }

    public void setGiftPackage(GiftPackage giftPackage) {
        this.giftPackage = giftPackage;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public BigDecimal getGiftWrapFee() {
        return giftWrapFee;
    }

    public void setGiftWrapFee(BigDecimal giftWrapFee) {
        this.giftWrapFee = giftWrapFee;
    }

    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public List<OrderGiftDetail> getOrderGiftDetails() {
        return orderGiftDetails;
    }

    public void setOrderGiftDetails(List<OrderGiftDetail> orderGiftDetails) {
        this.orderGiftDetails = orderGiftDetails;
    }
}
