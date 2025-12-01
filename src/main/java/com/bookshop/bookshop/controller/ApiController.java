package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.dto.GiftOrderCreateDTO;
import com.bookshop.bookshop.dto.GiftOrderResponseDTO;
import com.bookshop.bookshop.dto.OrderItemDTO;
import com.bookshop.bookshop.entity.Book;
import com.bookshop.bookshop.entity.GiftPackage;
import com.bookshop.bookshop.entity.OrderGift;
import com.bookshop.bookshop.entity.User;
import com.bookshop.bookshop.service.BookService;
import com.bookshop.bookshop.service.GiftOrderService;
import com.bookshop.bookshop.service.GiftPackageService;
import com.bookshop.bookshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {

    @Autowired
    private BookService bookService;
    
    @Autowired
    private GiftPackageService giftPackageService;
    
    @Autowired
    private GiftOrderService giftOrderService;
    
    @Autowired
    private UserService userService;

    /**
     * Get all books with optional sorting and category filter
     * @param sortBy - field to sort by (title, price, author, rating)
     * @param sortOrder - asc or desc
     * @param category - optional category filter
     */
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String category) {
        try {
            List<Book> books;
            
            if (category != null && !category.isEmpty()) {
                if (sortBy != null && !sortBy.isEmpty()) {
                    books = bookService.getBooksByCategorySorted(category, sortBy, sortOrder);
                } else {
                    books = bookService.getBooksByCategory(category);
                }
            } else {
                if (sortBy != null && !sortBy.isEmpty()) {
                    books = bookService.getAllBooksSorted(sortBy, sortOrder);
                } else {
                    books = bookService.getAllBooks();
                }
            }
            
            // Add cache control headers for better performance
            return ResponseEntity.ok()
                    .cacheControl(org.springframework.http.CacheControl.maxAge(60, java.util.concurrent.TimeUnit.SECONDS))
                    .body(books);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get book by ID
     */
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id) {
        try {
            Book book = bookService.getBookById(id);
            if (book != null) {
                return ResponseEntity.ok(book);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Search books
     */
    @GetMapping("/books/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String query) {
        try {
            List<Book> books = bookService.searchBooks(query);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get books by category
     */
    @GetMapping("/books/category/{category}")
    public ResponseEntity<List<Book>> getBooksByCategory(@PathVariable String category) {
        try {
            List<Book> books = bookService.getBooksByCategory(category);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get all categories
     */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        try {
            List<Book> books = bookService.getAllBooks();
            List<String> categories = books.stream()
                    .map(Book::getCategory)
                    .filter(category -> category != null && !category.trim().isEmpty())
                    .distinct()
                    .sorted()
                    .toList();
            // Add cache control headers for better performance
            return ResponseEntity.ok()
                    .cacheControl(org.springframework.http.CacheControl.maxAge(300, java.util.concurrent.TimeUnit.SECONDS))
                    .body(categories);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get all gift packages
     */
    @GetMapping("/gift-packages")
    public ResponseEntity<List<GiftPackage>> getAllGiftPackages() {
        try {
            List<GiftPackage> packages = giftPackageService.getAllGiftPackages();
            // Add cache control headers for better performance
            return ResponseEntity.ok()
                    .cacheControl(org.springframework.http.CacheControl.maxAge(60, java.util.concurrent.TimeUnit.SECONDS))
                    .body(packages);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get gift package by ID
     */
    @GetMapping("/gift-packages/{id}")
    public ResponseEntity<GiftPackage> getGiftPackageById(@PathVariable Integer id) {
        try {
            GiftPackage giftPackage = giftPackageService.getGiftPackageById(id);
            if (giftPackage != null) {
                return ResponseEntity.ok(giftPackage);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Create gift order
     */
    @PostMapping("/gift-orders/create")
    public ResponseEntity<?> createGiftOrder(@RequestBody Map<String, Object> orderData,
                                             Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "User not authenticated");
                return ResponseEntity.status(401).body(error);
            }
            
            String username = authentication.getName();
            User user = userService.findByUserName(username);
            if (user == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "User not found");
                return ResponseEntity.badRequest().body(error);
            }
            
            // Build DTO from request data
            GiftOrderCreateDTO dto = new GiftOrderCreateDTO();
            dto.setRecipientName((String) orderData.get("recipientName"));
            dto.setRecipientPhone((String) orderData.get("recipientPhone"));
            dto.setRecipientAddress((String) orderData.get("recipientAddress"));
            dto.setMessage((String) orderData.get("message"));
            dto.setDeliveryMethod((String) orderData.get("deliveryMethod"));
            dto.setPaymentMethod((String) orderData.get("paymentMethod"));
            
            // Parse delivery date if provided
            String deliveryDateStr = (String) orderData.get("deliveryDate");
            if (deliveryDateStr != null && !deliveryDateStr.isEmpty()) {
                // Convert LocalDate to LocalDateTime at start of day
                dto.setDeliveryDate(java.time.LocalDate.parse(deliveryDateStr).atStartOfDay());
            }
            
            // Set gift package ID if provided
            Object giftPackageIdObj = orderData.get("giftPackageId");
            if (giftPackageIdObj != null) {
                dto.setGiftPackageId(Integer.valueOf(giftPackageIdObj.toString()));
            }
            
            // Set shipping fee if provided
            Object shippingFeeObj = orderData.get("shippingFee");
            if (shippingFeeObj != null) {
                dto.setShippingFee(Integer.valueOf(shippingFeeObj.toString()));
            }
            
            // Parse items
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> itemsData = (List<Map<String, Object>>) orderData.get("items");
            List<OrderItemDTO> items = itemsData.stream()
                    .map(item -> {
                        OrderItemDTO itemDTO = new OrderItemDTO();
                        itemDTO.setBookId(Integer.valueOf(item.get("bookId").toString()));
                        itemDTO.setQuantity(Integer.valueOf(item.get("quantity").toString()));
                        return itemDTO;
                    })
                    .collect(Collectors.toList());
            dto.setItems(items);
            
            // Create the order
            OrderGift createdOrder = giftOrderService.createGiftOrder(user.getUserId(), dto);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("orderGiftId", createdOrder.getOrderGiftId());
            response.put("message", "Gift order created successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * Get my gift orders
     */
    @GetMapping("/gift-orders/my-orders")
    public ResponseEntity<?> getMyGiftOrders(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).build();
            }
            
            String username = authentication.getName();
            User user = userService.findByUserName(username);
            if (user == null) {
                return ResponseEntity.badRequest().build();
            }
            
            List<OrderGift> orders = giftOrderService.getGiftOrdersByUser(user.getUserId());
            
            // Convert to Map to include all fields
            List<Map<String, Object>> response = orders.stream()
                .map(order -> {
                    Map<String, Object> dto = new HashMap<>();
                    dto.put("orderGiftId", order.getOrderGiftId());
                    dto.put("recipientName", order.getRecipientName());
                    dto.put("recipientPhone", order.getRecipientPhone());
                    dto.put("recipientAddress", order.getRecipientAddress());
                    dto.put("message", order.getMessage());
                    dto.put("totalAmount", order.getTotalAmount());
                    dto.put("status", order.getStatus());
                    dto.put("createAt", order.getCreateAt());
                    dto.put("deliveryMethod", order.getDeliveryMethod());
                    dto.put("paymentMethod", order.getPaymentMethod());
                    // Safely get gift package name without triggering lazy load issues
                    try {
                        if (order.getGiftPackage() != null) {
                            dto.put("giftPackageName", order.getGiftPackage().getPackageName());
                        }
                    } catch (Exception ignored) {
                        // Ignore lazy loading exceptions
                    }
                    return dto;
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get gift order by ID
     */
    @GetMapping("/gift-orders/{id}")
    public ResponseEntity<?> getGiftOrderById(@PathVariable Integer id, Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).build();
            }
            
            OrderGift orderGift = giftOrderService.getGiftOrderById(id);
            
            // Check if order belongs to current user or user is admin
            String username = authentication.getName();
            User user = userService.findByUserName(username);
            boolean isAdmin = user.getRole() != null && user.getRole().equals("ADMIN");
            
            if (!isAdmin && !orderGift.getUser().getUserId().equals(user.getUserId())) {
                return ResponseEntity.status(403).build();
            }
            
            // Convert to DTO
            Map<String, Object> orderDTO = new HashMap<>();
            orderDTO.put("orderGiftId", orderGift.getOrderGiftId());
            orderDTO.put("recipientName", orderGift.getRecipientName());
            orderDTO.put("recipientPhone", orderGift.getRecipientPhone());
            orderDTO.put("recipientAddress", orderGift.getRecipientAddress());
            orderDTO.put("message", orderGift.getMessage());
            orderDTO.put("totalAmount", orderGift.getTotalAmount());
            orderDTO.put("status", orderGift.getStatus());
            orderDTO.put("createAt", orderGift.getCreateAt());
            orderDTO.put("deliveryMethod", orderGift.getDeliveryMethod());
            orderDTO.put("deliveryDate", orderGift.getDeliveryDate());
            orderDTO.put("shippingFee", orderGift.getShippingFee());
            orderDTO.put("giftWrapFee", orderGift.getGiftWrapFee());
            orderDTO.put("paymentMethod", orderGift.getPaymentMethod());
            
            // Gift package info
            if (orderGift.getGiftPackage() != null) {
                Map<String, Object> packageInfo = new HashMap<>();
                packageInfo.put("giftPackageId", orderGift.getGiftPackage().getGiftPackageId());
                packageInfo.put("packageName", orderGift.getGiftPackage().getPackageName());
                packageInfo.put("description", orderGift.getGiftPackage().getDescription());
                packageInfo.put("packageFee", orderGift.getGiftPackage().getPackageFee());
                orderDTO.put("giftPackage", packageInfo);
            }
            
            // User info
            if (orderGift.getUser() != null) {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("userName", orderGift.getUser().getUserName());
                userInfo.put("fullName", orderGift.getUser().getFullName());
                userInfo.put("email", orderGift.getUser().getEmail());
                userInfo.put("phone", orderGift.getUser().getPhone());
                orderDTO.put("user", userInfo);
            }
            
            // Order gift details (books)
            List<Map<String, Object>> detailsList = new ArrayList<>();
            if (orderGift.getOrderGiftDetails() != null && !orderGift.getOrderGiftDetails().isEmpty()) {
                for (var detail : orderGift.getOrderGiftDetails()) {
                    Map<String, Object> detailDTO = new HashMap<>();
                    detailDTO.put("orderGiftDetailId", detail.getOrderGiftDetailId());
                    detailDTO.put("quantity", detail.getQuantity());
                    detailDTO.put("price", detail.getPrice());
                    
                    // Book info
                    if (detail.getBook() != null) {
                        Map<String, Object> bookInfo = new HashMap<>();
                        bookInfo.put("bookId", detail.getBook().getBookId());
                        bookInfo.put("title", detail.getBook().getTitle());
                        detailDTO.put("book", bookInfo);
                    }
                    
                    detailsList.add(detailDTO);
                }
            }
            orderDTO.put("orderGiftDetails", detailsList);
            
            return ResponseEntity.ok(orderDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Pay gift order (VNPay)
     */
    @PostMapping("/gift-orders/{id}/pay")
    public ResponseEntity<?> payGiftOrder(@PathVariable Integer id, Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "User not authenticated");
                return ResponseEntity.status(401).body(error);
            }
            
            OrderGift orderGift = giftOrderService.getGiftOrderById(id);
            
            // Check if order belongs to current user
            String username = authentication.getName();
            User user = userService.findByUserName(username);
            if (!orderGift.getUser().getUserId().equals(user.getUserId())) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Unauthorized");
                return ResponseEntity.status(403).body(error);
            }
            
            // Only allow payment if order is PENDING
            if (!"PENDING".equals(orderGift.getStatus())) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Cannot pay order with status: " + orderGift.getStatus());
                return ResponseEntity.badRequest().body(error);
            }
            
            // Update status to PAID
            giftOrderService.updateGiftOrderStatus(id, "PAID");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Gift order paid successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    /**
     * Cancel gift order
     */
    @PostMapping("/gift-orders/{id}/cancel")
    public ResponseEntity<?> cancelGiftOrder(@PathVariable Integer id, Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "User not authenticated");
                return ResponseEntity.status(401).body(error);
            }
            
            OrderGift orderGift = giftOrderService.getGiftOrderById(id);
            
            // Check if order belongs to current user
            String username = authentication.getName();
            User user = userService.findByUserName(username);
            if (!orderGift.getUser().getUserId().equals(user.getUserId())) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Unauthorized");
                return ResponseEntity.status(403).body(error);
            }
            
            // Only allow cancellation if order is PENDING
            if (!"PENDING".equals(orderGift.getStatus())) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Cannot cancel order with status: " + orderGift.getStatus());
                return ResponseEntity.badRequest().body(error);
            }
            
            giftOrderService.updateGiftOrderStatus(id, "CANCELLED");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Gift order cancelled successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/auth/me")
    public ResponseEntity<Map<String, Object>> getAuthMe(org.springframework.security.core.Authentication authentication) {
        try {
            Map<String, Object> response = new HashMap<>();
            if (authentication == null || !authentication.isAuthenticated()) {
                response.put("authenticated", false);
                return ResponseEntity.ok(response);
            }
            String username = authentication.getName();
            User user = userService.findByUserName(username);
            if (user == null) {
                response.put("authenticated", false);
                return ResponseEntity.ok(response);
            }
            response.put("authenticated", true);
            response.put("userId", user.getUserId());
            response.put("username", user.getUserName());
            response.put("fullName", user.getFullName());
            response.put("email", user.getEmail());
            response.put("phone", user.getPhone());
            response.put("address", user.getAddress());
            response.put("role", user.getRole());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
