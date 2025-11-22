package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.dto.Cart;
import com.bookshop.bookshop.dto.CartItemDTO;
import com.bookshop.bookshop.dto.OrderCreateDTO;
import com.bookshop.bookshop.dto.OrderItemDTO;
import com.bookshop.bookshop.entity.Order;
import com.bookshop.bookshop.entity.User;
import com.bookshop.bookshop.service.CartService;
import com.bookshop.bookshop.service.OrderService;
import com.bookshop.bookshop.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {
    
    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;

    public OrderController(OrderService orderService, UserService userService, CartService cartService) {
        this.orderService = orderService;
        this.userService = userService;
        this.cartService = cartService;
    }
    
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> createOrder(@RequestBody Map<String, String> shippingInfo) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            User user = userService.findByUserName(username);
            
            // Get cart items
            Cart cart = cartService.getCart();
            if (cart.getItems().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Giỏ hàng trống");
                return ResponseEntity.badRequest().body(error);
            }
            
            // Convert cart items to order items
            List<OrderItemDTO> orderItems = new ArrayList<>();
            for (CartItemDTO cartItem : cart.getItems()) {
                OrderItemDTO orderItem = new OrderItemDTO();
                orderItem.setBookId(cartItem.getBookId());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItems.add(orderItem);
            }
            
            OrderCreateDTO orderDTO = new OrderCreateDTO();
            orderDTO.setItems(orderItems);
            orderDTO.setRecipientName(shippingInfo.get("recipientName"));
            orderDTO.setRecipientPhone(shippingInfo.get("recipientPhone"));
            orderDTO.setRecipientEmail(shippingInfo.get("recipientEmail"));
            orderDTO.setShippingAddress(shippingInfo.get("shippingAddress"));
            orderDTO.setOrderNotes(shippingInfo.get("orderNotes"));
            
            Order order = orderService.createOrder(user.getUserId(), orderDTO);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đặt hàng thành công");
            response.put("orderId", order.getOrderId());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/api/my-orders")
    @ResponseBody
    public ResponseEntity<?> getMyOrders() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            User user = userService.findByUserName(username);
            List<Order> orders = orderService.getOrdersByUser(user.getUserId());
            
            // Convert to DTO
            List<Map<String, Object>> orderDTOs = new ArrayList<>();
            for (Order order : orders) {
                Map<String, Object> orderDTO = new HashMap<>();
                orderDTO.put("orderId", order.getOrderId());
                orderDTO.put("orderDate", order.getOrderDate());
                orderDTO.put("status", order.getStatus());
                orderDTO.put("totalAmount", order.getTotalAmount());
                
                // Thông tin giao hàng (nếu có)
                orderDTO.put("recipientName", order.getRecipientName());
                orderDTO.put("recipientPhone", order.getRecipientPhone());
                orderDTO.put("recipientEmail", order.getRecipientEmail());
                orderDTO.put("shippingAddress", order.getShippingAddress());
                orderDTO.put("orderNotes", order.getOrderNotes());
                
                // Thông tin người đặt hàng (từ User)
                User orderUser = order.getUser();
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("userName", orderUser.getUserName());
                userInfo.put("fullName", orderUser.getFullName());
                userInfo.put("email", orderUser.getEmail());
                userInfo.put("phone", orderUser.getPhone());
                userInfo.put("address", orderUser.getAddress());
                orderDTO.put("userInfo", userInfo);
                
                List<Map<String, Object>> items = new ArrayList<>();
                for (var item : order.getOrderItems()) {
                    Map<String, Object> itemDTO = new HashMap<>();
                    itemDTO.put("orderItemId", item.getOrderItemId());
                    itemDTO.put("bookId", item.getBook().getBookId());
                    itemDTO.put("bookTitle", item.getBook().getTitle());
                    itemDTO.put("quantity", item.getQuantity());
                    itemDTO.put("price", item.getPrice());
                    items.add(itemDTO);
                }
                orderDTO.put("orderItems", items);
                orderDTOs.add(orderDTO);
            }
            
            return ResponseEntity.ok(orderDTOs);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/api/{orderId}")
    @ResponseBody
    public ResponseEntity<?> getOrderDetail(@PathVariable Integer orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            
            // Convert to DTO
            Map<String, Object> orderDTO = new HashMap<>();
            orderDTO.put("orderId", order.getOrderId());
            orderDTO.put("orderDate", order.getOrderDate());
            orderDTO.put("status", order.getStatus());
            orderDTO.put("totalAmount", order.getTotalAmount());
            
            // Thông tin giao hàng (nếu có)
            orderDTO.put("recipientName", order.getRecipientName());
            orderDTO.put("recipientPhone", order.getRecipientPhone());
            orderDTO.put("recipientEmail", order.getRecipientEmail());
            orderDTO.put("shippingAddress", order.getShippingAddress());
            orderDTO.put("orderNotes", order.getOrderNotes());
            
            // Thông tin người đặt hàng (từ User)
            User orderUser = order.getUser();
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userName", orderUser.getUserName());
            userInfo.put("fullName", orderUser.getFullName());
            userInfo.put("email", orderUser.getEmail());
            userInfo.put("phone", orderUser.getPhone());
            userInfo.put("address", orderUser.getAddress());
            orderDTO.put("user", userInfo);
            
            // Order items
            List<Map<String, Object>> items = new ArrayList<>();
            for (var item : order.getOrderItems()) {
                Map<String, Object> itemDTO = new HashMap<>();
                itemDTO.put("orderItemId", item.getOrderItemId());
                itemDTO.put("quantity", item.getQuantity());
                itemDTO.put("price", item.getPrice());
                
                // Book info
                Map<String, Object> bookInfo = new HashMap<>();
                bookInfo.put("bookId", item.getBook().getBookId());
                bookInfo.put("title", item.getBook().getTitle());
                itemDTO.put("book", bookInfo);
                
                items.add(itemDTO);
            }
            orderDTO.put("orderItems", items);
            
            return ResponseEntity.ok(orderDTO);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/{orderId}/cancel")
    @ResponseBody
    public ResponseEntity<?> cancelOrder(@PathVariable Integer orderId) {
        try {
            orderService.cancelOrder(orderId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đơn hàng đã được hủy");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
