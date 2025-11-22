package com.bookshop.bookshop.dto;

import jakarta.validation.constraints.*;

public class MembershipCreateDTO {
    
    @NotBlank(message = "Membership type is required")
    private String membershipType;
    
    @NotNull(message = "Months is required")
    @Min(value = 1, message = "Months must be at least 1")
    private Integer months;
    
    private String bankType;

    // Constructors
    public MembershipCreateDTO() {}

    public MembershipCreateDTO(String membershipType, Integer months, String bankType) {
        this.membershipType = membershipType;
        this.months = months;
        this.bankType = bankType;
    }

    // Getters and Setters
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
}
