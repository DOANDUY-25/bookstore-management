package com.bookshop.bookshop.dto;

import java.math.BigDecimal;

public class OrderItemResponseDTO {
    private Integer orderItemId;
    private Integer bookId;
    private String bookTitle;
    private Integer quantity;
    private BigDecimal price;

    public OrderItemResponseDTO() {}

    public OrderItemResponseDTO(Integer orderItemId, Integer bookId, String bookTitle, 
                                Integer quantity, BigDecimal price) {
        this.orderItemId = orderItemId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters
    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
