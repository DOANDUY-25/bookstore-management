package com.bookshop.bookshop.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    
    private List<CartItemDTO> items = new ArrayList<>();
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    public void addItem(CartItemDTO item) {
        // Check if item already exists in cart
        for (CartItemDTO existingItem : items) {
            if (existingItem.getBookId().equals(item.getBookId())) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                existingItem.calculateSubtotal();
                calculateTotalAmount();
                return;
            }
        }
        // Add new item
        item.calculateSubtotal();
        items.add(item);
        calculateTotalAmount();
    }
    
    public void updateItemQuantity(Integer bookId, Integer quantity) {
        for (CartItemDTO item : items) {
            if (item.getBookId().equals(bookId)) {
                item.setQuantity(quantity);
                item.calculateSubtotal();
                calculateTotalAmount();
                return;
            }
        }
    }
    
    public void removeItem(Integer bookId) {
        items.removeIf(item -> item.getBookId().equals(bookId));
        calculateTotalAmount();
    }
    
    public void clear() {
        items.clear();
        totalAmount = BigDecimal.ZERO;
    }
    
    public void calculateTotalAmount() {
        totalAmount = items.stream()
                .map(CartItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Constructors
    public Cart() {}

    public Cart(List<CartItemDTO> items, BigDecimal totalAmount) {
        this.items = items;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
