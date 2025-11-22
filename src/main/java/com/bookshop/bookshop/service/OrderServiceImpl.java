package com.bookshop.bookshop.service;

import com.bookshop.bookshop.dto.OrderCreateDTO;
import com.bookshop.bookshop.dto.OrderItemDTO;
import com.bookshop.bookshop.entity.*;
import com.bookshop.bookshop.exception.InsufficientStockException;
import com.bookshop.bookshop.exception.ResourceNotFoundException;
import com.bookshop.bookshop.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final MembershipRepository membershipRepository;
    private final CartService cartService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, 
                           UserRepository userRepository, BookRepository bookRepository, 
                           MembershipRepository membershipRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.membershipRepository = membershipRepository;
        this.cartService = cartService;
    }
    
    @Override
    public Order createOrder(Integer userId, OrderCreateDTO dto) {
        log.info("Creating order for user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        
        // Set shipping information
        order.setRecipientName(dto.getRecipientName());
        order.setRecipientPhone(dto.getRecipientPhone());
        order.setRecipientEmail(dto.getRecipientEmail());
        order.setShippingAddress(dto.getShippingAddress());
        order.setOrderNotes(dto.getOrderNotes());
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        // Create order items
        for (OrderItemDTO itemDTO : dto.getItems()) {
            Book book = bookRepository.findById(itemDTO.getBookId())
                    .orElseThrow(() -> new ResourceNotFoundException("Book", "id", itemDTO.getBookId()));
            
            // Check stock
            if (book.getStock() < itemDTO.getQuantity()) {
                throw new InsufficientStockException(book.getBookId(), itemDTO.getQuantity(), book.getStock());
            }
            
            // Reduce stock
            book.setStock(book.getStock() - itemDTO.getQuantity());
            bookRepository.save(book);
            
            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(book.getPrice());
            
            order.getOrderItems().add(orderItem);
            
            // Calculate total
            BigDecimal itemTotal = book.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }
        
        // Apply membership discount if available
        Optional<Membership> membershipOpt = membershipRepository.findByUserAndStatus(user, "ACTIVE");
        if (membershipOpt.isPresent()) {
            Membership membership = membershipOpt.get();
            BigDecimal discount = totalAmount.multiply(membership.getDiscountPercent())
                    .divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
            totalAmount = totalAmount.subtract(discount).setScale(2, java.math.RoundingMode.HALF_UP);
            log.info("Applied membership discount: {}% - Discount amount: {}", membership.getDiscountPercent(), discount);
        }
        
        order.setTotalAmount(totalAmount);
        
        Order savedOrder = orderRepository.save(order);
        
        // Clear cart
        cartService.clearCart();
        
        log.info("Order created successfully with ID: {}", savedOrder.getOrderId());
        return savedOrder;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Order getOrderById(Integer orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    @Override
    public Order updateOrderStatus(Integer orderId, String status) {
        log.info("Updating order {} status to {}", orderId, status);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        
        order.setStatus(status);
        
        Order updatedOrder = orderRepository.save(order);
        log.info("Order status updated successfully");
        
        return updatedOrder;
    }
    
    @Override
    public void cancelOrder(Integer orderId) {
        log.info("Cancelling order: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        
        // Restore stock for all items
        for (OrderItem item : order.getOrderItems()) {
            Book book = item.getBook();
            book.setStock(book.getStock() + item.getQuantity());
            bookRepository.save(book);
        }
        
        order.setStatus("CANCELLED");
        orderRepository.save(order);
        
        log.info("Order cancelled successfully");
    }
}
