package com.bookshop.bookshop.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class GiftPackageDTO {
    
    @NotBlank(message = "Package name is required")
    @Size(max = 100, message = "Package name must not exceed 100 characters")
    private String packageName;
    
    @Size(max = 100, message = "Gift box must not exceed 100 characters")
    private String giftBox;
    
    @Size(max = 100, message = "Greeting card must not exceed 100 characters")
    private String greetingCard;
    
    @Size(max = 100, message = "Paper type must not exceed 100 characters")
    private String paperType;
    
    private String description;
    
    @DecimalMin(value = "0.0", message = "Package fee must be at least 0")
    @Digits(integer = 10, fraction = 2, message = "Package fee must have at most 10 integer digits and 2 decimal places")
    private BigDecimal packageFee;
    
    @Size(max = 255, message = "Image URL must not exceed 255 characters")
    private String imageURL;

    // Constructors
    public GiftPackageDTO() {}

    public GiftPackageDTO(String packageName, String giftBox, String greetingCard, 
                         String paperType, String description, BigDecimal packageFee, String imageURL) {
        this.packageName = packageName;
        this.giftBox = giftBox;
        this.greetingCard = greetingCard;
        this.paperType = paperType;
        this.description = description;
        this.packageFee = packageFee;
        this.imageURL = imageURL;
    }

    // Getters and Setters
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getGiftBox() {
        return giftBox;
    }

    public void setGiftBox(String giftBox) {
        this.giftBox = giftBox;
    }

    public String getGreetingCard() {
        return greetingCard;
    }

    public void setGreetingCard(String greetingCard) {
        this.greetingCard = greetingCard;
    }

    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPackageFee() {
        return packageFee;
    }

    public void setPackageFee(BigDecimal packageFee) {
        this.packageFee = packageFee;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
