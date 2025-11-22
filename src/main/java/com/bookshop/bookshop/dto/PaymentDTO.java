package com.bookshop.bookshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PaymentDTO {
    
    @NotBlank(message = "Payment method is required")
    @Size(max = 50, message = "Payment method must not exceed 50 characters")
    private String paymentMethod;
    
    @Size(max = 100, message = "Transaction ID must not exceed 100 characters")
    private String transactionId;

    // Constructors
    public PaymentDTO() {}

    public PaymentDTO(String paymentMethod, String transactionId) {
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
    }

    // Getters and Setters
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
}
