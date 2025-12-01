package com.bookshop.bookshop.service;

import com.bookshop.bookshop.dto.GiftOrderCreateDTO;
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
public class GiftOrderServiceImpl implements GiftOrderService {
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GiftOrderServiceImpl.class);
    private final OrderGiftRepository orderGiftRepository;
    private final OrderGiftDetailRepository orderGiftDetailRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final GiftPackageRepository giftPackageRepository;
    private final MembershipRepository membershipRepository;

    public GiftOrderServiceImpl(OrderGiftRepository orderGiftRepository, 
                               OrderGiftDetailRepository orderGiftDetailRepository, 
                               UserRepository userRepository, BookRepository bookRepository, 
                               GiftPackageRepository giftPackageRepository,
                               MembershipRepository membershipRepository) {
        this.orderGiftRepository = orderGiftRepository;
        this.orderGiftDetailRepository = orderGiftDetailRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.giftPackageRepository = giftPackageRepository;
        this.membershipRepository = membershipRepository;
    }
    
    @Override
    public OrderGift createGiftOrder(Integer userId, GiftOrderCreateDTO dto) {
        log.info("Creating gift order for user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        OrderGift orderGift = new OrderGift();
        orderGift.setUser(user);
        orderGift.setRecipientName(dto.getRecipientName());
        orderGift.setRecipientPhone(dto.getRecipientPhone());
        orderGift.setRecipientAddress(dto.getRecipientAddress());
        orderGift.setMessage(dto.getMessage());
        orderGift.setDeliveryDate(dto.getDeliveryDate());
        orderGift.setDeliveryMethod(dto.getDeliveryMethod());
        orderGift.setPaymentMethod(dto.getPaymentMethod());
        orderGift.setCreateAt(LocalDateTime.now());
        orderGift.setStatus("PENDING");
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        // Add gift package if selected
        if (dto.getGiftPackageId() != null) {
            GiftPackage giftPackage = giftPackageRepository.findById(dto.getGiftPackageId())
                    .orElseThrow(() -> new ResourceNotFoundException("GiftPackage", "id", dto.getGiftPackageId()));
            orderGift.setGiftPackage(giftPackage);
            orderGift.setGiftWrapFee(giftPackage.getPackageFee());
            totalAmount = totalAmount.add(giftPackage.getPackageFee());
        } else {
            orderGift.setGiftWrapFee(BigDecimal.ZERO);
        }
        
        // Set shipping fee from DTO or use default
        BigDecimal shippingFee = dto.getShippingFee() != null ? 
            BigDecimal.valueOf(dto.getShippingFee()) : BigDecimal.valueOf(30000);
        
        // Check if user has GOLD or PLATINUM membership - free shipping
        Optional<Membership> membershipOpt = membershipRepository.findByUserAndStatus(user, "ACTIVE");
        if (membershipOpt.isPresent()) {
            Membership membership = membershipOpt.get();
            String membershipType = membership.getMembershipType();
            if ("GOLD".equalsIgnoreCase(membershipType) || "PLATINUM".equalsIgnoreCase(membershipType)) {
                shippingFee = BigDecimal.ZERO;
                log.info("Free shipping applied for {} membership", membershipType);
            }
        }
        
        orderGift.setShippingFee(shippingFee);
        totalAmount = totalAmount.add(shippingFee);
        
        // Calculate books total first
        BigDecimal booksTotal = BigDecimal.ZERO;
        for (OrderItemDTO itemDTO : dto.getItems()) {
            Book book = bookRepository.findById(itemDTO.getBookId())
                    .orElseThrow(() -> new ResourceNotFoundException("Book", "id", itemDTO.getBookId()));
            
            // Check stock
            if (book.getStock() < itemDTO.getQuantity()) {
                throw new InsufficientStockException(book.getBookId(), itemDTO.getQuantity(), book.getStock());
            }
            
            BigDecimal itemSubtotal = book.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            booksTotal = booksTotal.add(itemSubtotal);
        }
        
        // Set total amount
        totalAmount = totalAmount.add(booksTotal);
        orderGift.setTotalAmount(totalAmount);
        
        // Save order first to get ID
        OrderGift savedOrderGift = orderGiftRepository.save(orderGift);
        
        // Now create order gift details
        for (OrderItemDTO itemDTO : dto.getItems()) {
            Book book = bookRepository.findById(itemDTO.getBookId())
                    .orElseThrow(() -> new ResourceNotFoundException("Book", "id", itemDTO.getBookId()));
            
            // Reduce stock
            book.setStock(book.getStock() - itemDTO.getQuantity());
            bookRepository.save(book);
            
            // Create order gift detail
            OrderGiftDetail detail = new OrderGiftDetail();
            detail.setOrderGift(savedOrderGift);
            detail.setBook(book);
            detail.setQuantity(itemDTO.getQuantity());
            detail.setPrice(book.getPrice());
            detail.setSubtotal(book.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
            
            orderGiftDetailRepository.save(detail);
        }
        
        log.info("Gift order created successfully with ID: {}", savedOrderGift.getOrderGiftId());
        
        return savedOrderGift;
    }
    
    @Override
    @Transactional(readOnly = true)
    public OrderGift getGiftOrderById(Integer orderGiftId) {
        // Use custom query with JOIN FETCH to eagerly load all related entities
        return orderGiftRepository.findByIdWithDetails(orderGiftId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderGift", "id", orderGiftId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<OrderGift> getGiftOrdersByUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return orderGiftRepository.findByUserOrderByCreateAtDesc(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<OrderGift> getAllGiftOrders() {
        return orderGiftRepository.findAll();
    }
    
    @Override
    public OrderGift updateGiftOrderStatus(Integer orderGiftId, String status) {
        log.info("Updating gift order {} status to {}", orderGiftId, status);
        
        OrderGift orderGift = orderGiftRepository.findById(orderGiftId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderGift", "id", orderGiftId));
        
        orderGift.setStatus(status);
        
        OrderGift updatedOrderGift = orderGiftRepository.save(orderGift);
        log.info("Gift order status updated successfully");
        
        return updatedOrderGift;
    }
}
