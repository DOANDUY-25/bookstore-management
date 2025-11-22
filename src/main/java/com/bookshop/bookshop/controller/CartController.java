package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.dto.Cart;
import com.bookshop.bookshop.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    
    @GetMapping
    public String viewCart() {
        return "redirect:/cart.html";
    }
    
    @GetMapping("/api")
    @ResponseBody
    public Cart getCartApi() {
        return cartService.getCart();
    }
    
    @PostMapping("/add")
    @ResponseBody
    public String addToCart(@RequestParam Integer bookId,
                           @RequestParam Integer quantity) {
        try {
            cartService.addToCart(bookId, quantity);
            return "{\"success\": true, \"message\": \"Book added to cart successfully\"}";
        } catch (Exception e) {
            return "{\"success\": false, \"message\": \"" + e.getMessage() + "\"}";
        }
    }
    
    @PostMapping("/update")
    @ResponseBody
    public String updateCartItem(@RequestParam Integer bookId,
                                 @RequestParam Integer quantity) {
        try {
            cartService.updateCartItem(bookId, quantity);
            return "{\"success\": true, \"message\": \"Cart updated successfully\"}";
        } catch (Exception e) {
            return "{\"success\": false, \"message\": \"" + e.getMessage() + "\"}";
        }
    }
    
    @PostMapping("/remove")
    @ResponseBody
    public String removeFromCart(@RequestParam Integer bookId) {
        try {
            cartService.removeFromCart(bookId);
            return "{\"success\": true, \"message\": \"Item removed from cart\"}";
        } catch (Exception e) {
            return "{\"success\": false, \"message\": \"" + e.getMessage() + "\"}";
        }
    }
}
