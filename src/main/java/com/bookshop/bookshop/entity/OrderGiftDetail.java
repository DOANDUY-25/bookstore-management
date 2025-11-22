package com.bookshop.bookshop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ordergiftdetail")
public class OrderGiftDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderGiftDetailId")
    private Integer orderGiftDetailId;
    
    @NotNull(message = "Order gift is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderGiftId", nullable = false)
    private OrderGift orderGift;
    
    @NotNull(message = "Book is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookId", nullable = false)
    private Book book;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "quantity")
    private Integer quantity;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be at least 0")
    @Digits(integer = 10, fraction = 2, message = "Price must have at most 10 integer digits and 2 decimal places")
    @Column(name = "price", precision = 12, scale = 2)
    private BigDecimal price;
    
    @DecimalMin(value = "0.0", message = "Subtotal must be at least 0")
    @Digits(integer = 10, fraction = 2, message = "Subtotal must have at most 10 integer digits and 2 decimal places")
    @Column(name = "subtotal", precision = 12, scale = 2)
    private BigDecimal subtotal;

    // Constructors
    public OrderGiftDetail() {}

    public OrderGiftDetail(Integer orderGiftDetailId, OrderGift orderGift, Book book, 
                          Integer quantity, BigDecimal price, BigDecimal subtotal) {
        this.orderGiftDetailId = orderGiftDetailId;
        this.orderGift = orderGift;
        this.book = book;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
    }

    // Getters and Setters
    public Integer getOrderGiftDetailId() {
        return orderGiftDetailId;
    }

    public void setOrderGiftDetailId(Integer orderGiftDetailId) {
        this.orderGiftDetailId = orderGiftDetailId;
    }

    public OrderGift getOrderGift() {
        return orderGift;
    }

    public void setOrderGift(OrderGift orderGift) {
        this.orderGift = orderGift;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
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

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
