package com.bookshop.bookshop.dto;

import java.math.BigDecimal;

public class CartItemDTO {
    
    private Integer bookId;
    private String title;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
    private String coverImageURL;

    // Constructors
    public CartItemDTO() {}

    public CartItemDTO(Integer bookId, String title, BigDecimal price, 
                      Integer quantity, BigDecimal subtotal, String coverImageURL) {
        this.bookId = bookId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.subtotal = subtotal;
        this.coverImageURL = coverImageURL;
    }
    
    public void calculateSubtotal() {
        if (price != null && quantity != null) {
            this.subtotal = price.multiply(BigDecimal.valueOf(quantity));
        }
    }

    // Getters and Setters
    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getCoverImageURL() {
        return coverImageURL;
    }

    public void setCoverImageURL(String coverImageURL) {
        this.coverImageURL = coverImageURL;
    }
}
