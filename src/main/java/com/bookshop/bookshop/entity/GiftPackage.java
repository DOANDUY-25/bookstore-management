package com.bookshop.bookshop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "giftpackage")
public class GiftPackage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "giftPackageId")
    private Integer giftPackageId;
    
    @NotBlank(message = "Package name is required")
    @Size(max = 100, message = "Package name must not exceed 100 characters")
    @Column(name = "packageName", length = 100)
    private String packageName;
    
    @Size(max = 100, message = "Gift box must not exceed 100 characters")
    @Column(name = "giftBox", length = 100)
    private String giftBox;
    
    @Size(max = 100, message = "Greeting card must not exceed 100 characters")
    @Column(name = "greetingCard", length = 100)
    private String greetingCard;
    
    @Size(max = 100, message = "Paper type must not exceed 100 characters")
    @Column(name = "paperType", length = 100)
    private String paperType;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @DecimalMin(value = "0.0", message = "Package fee must be at least 0")
    @Digits(integer = 10, fraction = 2, message = "Package fee must have at most 10 integer digits and 2 decimal places")
    @Column(name = "packageFee", precision = 12, scale = 2)
    private BigDecimal packageFee;
    
    @Size(max = 255, message = "Image URL must not exceed 255 characters")
    @Column(name = "imageURL", length = 255)
    private String imageURL;

    // Constructors
    public GiftPackage() {}

    public GiftPackage(Integer giftPackageId, String packageName, String giftBox, 
                      String greetingCard, String paperType, String description, 
                      BigDecimal packageFee, String imageURL) {
        this.giftPackageId = giftPackageId;
        this.packageName = packageName;
        this.giftBox = giftBox;
        this.greetingCard = greetingCard;
        this.paperType = paperType;
        this.description = description;
        this.packageFee = packageFee;
        this.imageURL = imageURL;
    }

    // Getters and Setters
    public Integer getGiftPackageId() {
        return giftPackageId;
    }

    public void setGiftPackageId(Integer giftPackageId) {
        this.giftPackageId = giftPackageId;
    }

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
