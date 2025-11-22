package com.bookshop.bookshop.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GiftOrderResponseDTO {
    private Integer orderGiftId;
    private String recipientName;
    private String recipientPhone;
    private String recipientAddress;
    private String message;
    private String giftPackageName;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime createAt;
    private String deliveryMethod;

    // Constructors
    public GiftOrderResponseDTO() {}

    // Getters and Setters
    public Integer getOrderGiftId() {
        return orderGiftId;
    }

    public void setOrderGiftId(Integer orderGiftId) {
        this.orderGiftId = orderGiftId;
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

    public String getGiftPackageName() {
        return giftPackageName;
    }

    public void setGiftPackageName(String giftPackageName) {
        this.giftPackageName = giftPackageName;
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

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }
}
