package com.bookshop.bookshop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "membership")
public class Membership {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @NotBlank(message = "Membership type is required")
    @Size(max = 50, message = "Membership type must not exceed 50 characters")
    @Column(name = "membershipType", length = 50)
    private String membershipType;
    
    @NotNull(message = "Months is required")
    @Min(value = 1, message = "Months must be at least 1")
    @Column(name = "months")
    private Integer months;
    
    @Size(max = 50, message = "Bank type must not exceed 50 characters")
    @Column(name = "bankType", length = 50)
    private String bankType;
    
    @NotNull(message = "Discount percent is required")
    @DecimalMin(value = "0.0", message = "Discount percent must be at least 0")
    @DecimalMax(value = "100.0", message = "Discount percent must not exceed 100")
    @Digits(integer = 3, fraction = 2, message = "Discount percent must have at most 3 integer digits and 2 decimal places")
    @Column(name = "discountPercent", precision = 5, scale = 2)
    private BigDecimal discountPercent;
    
    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", message = "Total amount must be at least 0")
    @Digits(integer = 10, fraction = 2, message = "Total amount must have at most 10 integer digits and 2 decimal places")
    @Column(name = "totalAmount", precision = 12, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(name = "createdAt")
    private LocalDateTime createdAt;
    
    @Column(name = "expiresAt")
    private LocalDateTime expiresAt;
    
    @Column(name = "status", length = 50)
    private String status = "ACTIVE";
    
    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (expiresAt == null && months != null) {
            expiresAt = createdAt.plusMonths(months);
        }
        if (status == null) {
            status = "ACTIVE";
        }
    }

    // Constructors
    public Membership() {}

    public Membership(Integer id, String membershipType, Integer months, String bankType, 
                     BigDecimal discountPercent, BigDecimal totalAmount, LocalDateTime createdAt, 
                     LocalDateTime expiresAt, String status, User user) {
        this.id = id;
        this.membershipType = membershipType;
        this.months = months;
        this.bankType = bankType;
        this.discountPercent = discountPercent;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.status = status;
        this.user = user;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public Integer getMonths() {
        return months;
    }

    public void setMonths(Integer months) {
        this.months = months;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
