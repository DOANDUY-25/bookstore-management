package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.entity.Order;
import com.bookshop.bookshop.entity.OrderGift;
import com.bookshop.bookshop.entity.User;
import com.bookshop.bookshop.entity.Membership;
import com.bookshop.bookshop.service.OrderService;
import com.bookshop.bookshop.service.GiftOrderService;
import com.bookshop.bookshop.service.UserService;
import com.bookshop.bookshop.service.MembershipService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    private final OrderService orderService;
    private final GiftOrderService giftOrderService;
    private final UserService userService;
    private final MembershipService membershipService;
    private final com.bookshop.bookshop.repository.BookRepository bookRepository;

    public AdminController(OrderService orderService, GiftOrderService giftOrderService, 
                          UserService userService, MembershipService membershipService,
                          com.bookshop.bookshop.repository.BookRepository bookRepository) {
        this.orderService = orderService;
        this.giftOrderService = giftOrderService;
        this.userService = userService;
        this.membershipService = membershipService;
        this.bookRepository = bookRepository;
    }
    
    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders() {
        try {
            List<Order> orders = orderService.getAllOrders();
            // Convert to simple DTO to avoid lazy loading issues
            List<java.util.Map<String, Object>> result = orders.stream().map(o -> {
                java.util.Map<String, Object> map = new java.util.HashMap<>();
                map.put("orderId", o.getOrderId());
                map.put("orderDate", o.getOrderDate());
                map.put("totalAmount", o.getTotalAmount());
                map.put("status", o.getStatus());
                try {
                    if (o.getUser() != null) {
                        java.util.Map<String, Object> userMap = new java.util.HashMap<>();
                        userMap.put("userId", o.getUser().getUserId());
                        userMap.put("userName", o.getUser().getUserName());
                        userMap.put("fullName", o.getUser().getFullName());
                        map.put("user", userMap);
                    }
                } catch (Exception e) {
                    map.put("user", null);
                }
                return map;
            }).collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(java.util.Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/gift-orders")
    public ResponseEntity<?> getAllGiftOrders() {
        try {
            List<OrderGift> giftOrders = giftOrderService.getAllGiftOrders();
            List<java.util.Map<String, Object>> result = giftOrders.stream().map(o -> {
                java.util.Map<String, Object> map = new java.util.HashMap<>();
                map.put("orderGiftId", o.getOrderGiftId());
                map.put("recipientName", o.getRecipientName());
                map.put("totalAmount", o.getTotalAmount());
                map.put("status", o.getStatus());
                try {
                    if (o.getUser() != null) {
                        java.util.Map<String, Object> userMap = new java.util.HashMap<>();
                        userMap.put("userId", o.getUser().getUserId());
                        userMap.put("userName", o.getUser().getUserName());
                        userMap.put("fullName", o.getUser().getFullName());
                        map.put("user", userMap);
                    }
                } catch (Exception e) {
                    map.put("user", null);
                }
                return map;
            }).collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(java.util.Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(java.util.Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/memberships")
    public ResponseEntity<?> getAllMemberships() {
        try {
            List<Membership> memberships = membershipService.getAllMemberships();
            List<java.util.Map<String, Object>> result = memberships.stream().map(m -> {
                java.util.Map<String, Object> map = new java.util.HashMap<>();
                map.put("membershipId", m.getId());
                map.put("membershipType", m.getMembershipType());
                map.put("startDate", m.getCreatedAt());
                map.put("endDate", m.getExpiresAt());
                map.put("status", m.getStatus());
                try {
                    if (m.getUser() != null) {
                        java.util.Map<String, Object> userMap = new java.util.HashMap<>();
                        userMap.put("userId", m.getUser().getUserId());
                        userMap.put("userName", m.getUser().getUserName());
                        userMap.put("fullName", m.getUser().getFullName());
                        map.put("user", userMap);
                    }
                } catch (Exception e) {
                    map.put("user", null);
                }
                return map;
            }).collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(java.util.Map.of("error", e.getMessage()));
        }
    }
    
    // Book Management APIs
    @PostMapping("/books")
    public ResponseEntity<?> createBook(@RequestBody com.bookshop.bookshop.entity.Book book) {
        try {
            com.bookshop.bookshop.entity.Book savedBook = bookRepository.save(book);
            return ResponseEntity.ok(savedBook);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/books/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Integer id, @RequestBody com.bookshop.bookshop.entity.Book book) {
        try {
            book.setBookId(id);
            com.bookshop.bookshop.entity.Book updatedBook = bookRepository.save(book);
            return ResponseEntity.ok(updatedBook);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }
    
    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Integer id) {
        try {
            bookRepository.deleteById(id);
            return ResponseEntity.ok(java.util.Map.of("success", true, "message", "Book deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/orders/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Integer id) {
        try {
            Order order = orderService.getOrderById(id);
            if (order == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("error", "Order not found"));
            }
            orderService.updateOrderStatus(id, "CANCELLED");
            return ResponseEntity.ok(java.util.Map.of("success", true, "message", "Order cancelled successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/gift-orders/{id}/cancel")
    public ResponseEntity<?> cancelGiftOrder(@PathVariable Integer id) {
        try {
            OrderGift giftOrder = giftOrderService.getGiftOrderById(id);
            if (giftOrder == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("error", "Gift order not found"));
            }
            giftOrderService.updateGiftOrderStatus(id, "CANCELLED");
            return ResponseEntity.ok(java.util.Map.of("success", true, "message", "Gift order cancelled successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Integer id, @RequestBody java.util.Map<String, String> body) {
        try {
            String role = body.get("role");
            if (role == null || (!role.equals("USER") && !role.equals("ADMIN"))) {
                return ResponseEntity.badRequest().body(java.util.Map.of("error", "Invalid role"));
            }
            userService.updateUserRole(id, role);
            return ResponseEntity.ok(java.util.Map.of("success", true, "message", "User role updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }
}
