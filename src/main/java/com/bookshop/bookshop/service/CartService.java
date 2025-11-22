package com.bookshop.bookshop.service;

import com.bookshop.bookshop.dto.Cart;

public interface CartService {
    
    void addToCart(Integer bookId, Integer quantity);
    
    Cart getCart();
    
    void updateCartItem(Integer bookId, Integer quantity);
    
    void removeFromCart(Integer bookId);
    
    void clearCart();
}
