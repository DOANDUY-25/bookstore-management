package com.bookshop.bookshop.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public class GiftOrderCreateDTO {
    
    @NotEmpty(message = "Gift order must contain at least one item")
    @Valid
    private List<OrderItemDTO> items;
    
    @NotBlank(message = "Recipient name is required")
    @Size(max = 255, message = "Recipient name must not exceed 255 characters")
    private String recipientName;
    
    @NotBlank(message = "Recipient phone is required")
    @Size(max = 20, message = "Recipient phone must not exceed 20 characters")
    private String recipientPhone;
    
    @NotBlank(message = "Recipient address is required")
    @Size(max = 255, message = "Recipient address must not exceed 255 characters")
    private String recipientAddress;
    
    private String message;
    
    private Integer giftPackageId;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deliveryDate;
    
    @Size(max = 100, message = "Delivery method must not exceed 100 characters")
    private String deliveryMethod;
    
    private Integer shippingFee;

    // Constructors
    public GiftOrderCreateDTO() {}

    public GiftOrderCreateDTO(List<OrderItemDTO> items, String recipientName, String recipientPhone, 
                             String recipientAddress, String message, Integer giftPackageId, 
                             LocalDateTime deliveryDate, String deliveryMethod) {
        this.items = items;
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
        this.recipientAddress = recipientAddress;
        this.message = message;
        this.giftPackageId = giftPackageId;
        this.deliveryDate = deliveryDate;
        this.deliveryMethod = deliveryMethod;
    }

    // Getters and Setters
    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
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

    public Integer getGiftPackageId() {
        return giftPackageId;
    }

    public void setGiftPackageId(Integer giftPackageId) {
        this.giftPackageId = giftPackageId;
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

    public Integer getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Integer shippingFee) {
        this.shippingFee = shippingFee;
    }
}
