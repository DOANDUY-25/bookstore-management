package com.bookshop.bookshop.service;

import com.bookshop.bookshop.dto.Cart;
import com.bookshop.bookshop.dto.CartItemDTO;
import com.bookshop.bookshop.entity.Book;
import com.bookshop.bookshop.exception.InsufficientStockException;
import com.bookshop.bookshop.exception.ResourceNotFoundException;
import com.bookshop.bookshop.repository.BookRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartServiceImpl implements CartService {
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CartServiceImpl.class);
    private final BookRepository bookRepository;
    private final HttpSession session;
    private static final String CART_SESSION_KEY = "cart";

    public CartServiceImpl(BookRepository bookRepository, HttpSession session) {
        this.bookRepository = bookRepository;
        this.session = session;
    }
    
    @Override
    @Transactional(readOnly = true)
    public void addToCart(Integer bookId, Integer quantity) {
        log.info("Adding book {} with quantity {} to cart", bookId, quantity);
        
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));
        
        // Check stock availability
        if (book.getStock() < quantity) {
            throw new InsufficientStockException(bookId, quantity, book.getStock());
        }
        
        Cart cart = getCart();
        
        // Check if adding this quantity would exceed stock
        for (CartItemDTO item : cart.getItems()) {
            if (item.getBookId().equals(bookId)) {
                int totalQuantity = item.getQuantity() + quantity;
                if (book.getStock() < totalQuantity) {
                    throw new InsufficientStockException(bookId, totalQuantity, book.getStock());
                }
            }
        }
        
        CartItemDTO cartItem = new CartItemDTO();
        cartItem.setBookId(book.getBookId());
        cartItem.setTitle(book.getTitle());
        cartItem.setPrice(book.getPrice());
        cartItem.setQuantity(quantity);
        cartItem.setCoverImageURL(book.getCoverImageURL());
        
        cart.addItem(cartItem);
        session.setAttribute(CART_SESSION_KEY, cart);
        
        log.info("Book added to cart successfully");
    }
    
    @Override
    public Cart getCart() {
        Cart cart = (Cart) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new Cart();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }
    
    @Override
    @Transactional(readOnly = true)
    public void updateCartItem(Integer bookId, Integer quantity) {
        log.info("Updating cart item {} to quantity {}", bookId, quantity);
        
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));
        
        // Check stock availability
        if (book.getStock() < quantity) {
            throw new InsufficientStockException(bookId, quantity, book.getStock());
        }
        
        Cart cart = getCart();
        cart.updateItemQuantity(bookId, quantity);
        session.setAttribute(CART_SESSION_KEY, cart);
        
        log.info("Cart item updated successfully");
    }
    
    @Override
    public void removeFromCart(Integer bookId) {
        log.info("Removing book {} from cart", bookId);
        
        Cart cart = getCart();
        cart.removeItem(bookId);
        session.setAttribute(CART_SESSION_KEY, cart);
        
        log.info("Book removed from cart successfully");
    }
    
    @Override
    public void clearCart() {
        log.info("Clearing cart");
        session.removeAttribute(CART_SESSION_KEY);
        log.info("Cart cleared successfully");
    }
}
