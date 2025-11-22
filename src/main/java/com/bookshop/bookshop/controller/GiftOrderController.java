package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.dto.GiftOrderCreateDTO;
import com.bookshop.bookshop.entity.GiftPackage;
import com.bookshop.bookshop.entity.OrderGift;
import com.bookshop.bookshop.entity.User;
import com.bookshop.bookshop.service.GiftOrderService;
import com.bookshop.bookshop.service.GiftPackageService;
import com.bookshop.bookshop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/gift-orders")
public class GiftOrderController {
    
    private final GiftOrderService giftOrderService;
    private final GiftPackageService giftPackageService;
    private final UserService userService;

    public GiftOrderController(GiftOrderService giftOrderService, GiftPackageService giftPackageService, UserService userService) {
        this.giftOrderService = giftOrderService;
        this.giftPackageService = giftPackageService;
        this.userService = userService;
    }
    
    @GetMapping("/packages")
    public String viewGiftPackages(Model model) {
        List<GiftPackage> packages = giftPackageService.getAllGiftPackages();
        model.addAttribute("packages", packages);
        return "gift-orders/packages";
    }
    
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("giftOrder", new GiftOrderCreateDTO());
        List<GiftPackage> packages = giftPackageService.getAllGiftPackages();
        model.addAttribute("packages", packages);
        return "gift-orders/create";
    }
    
    @PostMapping("/create")
    public String createGiftOrder(@Valid @ModelAttribute("giftOrder") GiftOrderCreateDTO dto,
                                  BindingResult result,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        if (result.hasErrors()) {
            List<GiftPackage> packages = giftPackageService.getAllGiftPackages();
            model.addAttribute("packages", packages);
            return "gift-orders/create";
        }
        
        try {
            User user = userService.findByUserName(authentication.getName());
            OrderGift orderGift = giftOrderService.createGiftOrder(user.getUserId(), dto);
            redirectAttributes.addFlashAttribute("success", "Gift order created successfully");
            return "redirect:/gift-orders/" + orderGift.getOrderGiftId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/gift-orders/create";
        }
    }
    
    @GetMapping
    public String viewGiftOrderHistory(Authentication authentication, Model model) {
        User user = userService.findByUserName(authentication.getName());
        List<OrderGift> giftOrders = giftOrderService.getGiftOrdersByUser(user.getUserId());
        model.addAttribute("giftOrders", giftOrders);
        return "gift-orders/list";
    }
    
    @GetMapping("/{id}")
    public String viewGiftOrderDetails(@PathVariable Integer id, Model model) {
        OrderGift orderGift = giftOrderService.getGiftOrderById(id);
        model.addAttribute("orderGift", orderGift);
        return "gift-orders/details";
    }
    
    // API Endpoints
    @GetMapping("/api/my-orders")
    @ResponseBody
    public ResponseEntity<?> getMyGiftOrders() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            User user = userService.findByUserName(username);
            List<OrderGift> giftOrders = giftOrderService.getGiftOrdersByUser(user.getUserId());
            
            // Convert to DTO
            List<Map<String, Object>> orderDTOs = new ArrayList<>();
            for (OrderGift order : giftOrders) {
                Map<String, Object> orderDTO = new HashMap<>();
                orderDTO.put("orderGiftId", order.getOrderGiftId());
                orderDTO.put("createAt", order.getCreateAt());
                orderDTO.put("status", order.getStatus());
                orderDTO.put("totalAmount", order.getTotalAmount());
                orderDTO.put("recipientName", order.getRecipientName());
                orderDTO.put("recipientPhone", order.getRecipientPhone());
                orderDTO.put("recipientAddress", order.getRecipientAddress());
                orderDTO.put("message", order.getMessage());
                
                // Gift package info
                Map<String, Object> packageInfo = new HashMap<>();
                packageInfo.put("packageId", order.getGiftPackage().getGiftPackageId());
                packageInfo.put("packageName", order.getGiftPackage().getPackageName());
                orderDTO.put("giftPackage", packageInfo);
                
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
    public ResponseEntity<?> getGiftOrderDetail(@PathVariable Integer orderId) {
        try {
            OrderGift order = giftOrderService.getGiftOrderById(orderId);
            
            // Convert to DTO
            Map<String, Object> orderDTO = new HashMap<>();
            orderDTO.put("orderGiftId", order.getOrderGiftId());
            orderDTO.put("createAt", order.getCreateAt());
            orderDTO.put("status", order.getStatus());
            orderDTO.put("totalAmount", order.getTotalAmount());
            orderDTO.put("recipientName", order.getRecipientName());
            orderDTO.put("recipientPhone", order.getRecipientPhone());
            orderDTO.put("recipientAddress", order.getRecipientAddress());
            orderDTO.put("message", order.getMessage());
            
            // Gift package info
            Map<String, Object> packageInfo = new HashMap<>();
            packageInfo.put("packageId", order.getGiftPackage().getGiftPackageId());
            packageInfo.put("packageName", order.getGiftPackage().getPackageName());
            packageInfo.put("description", order.getGiftPackage().getDescription());
            packageInfo.put("packageFee", order.getGiftPackage().getPackageFee());
            orderDTO.put("giftPackage", packageInfo);
            
            // User info
            User orderUser = order.getUser();
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userName", orderUser.getUserName());
            userInfo.put("fullName", orderUser.getFullName());
            userInfo.put("email", orderUser.getEmail());
            userInfo.put("phone", orderUser.getPhone());
            orderDTO.put("user", userInfo);
            
            return ResponseEntity.ok(orderDTO);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/api/{orderId}/cancel")
    @ResponseBody
    public ResponseEntity<?> cancelGiftOrder(@PathVariable Integer orderId) {
        try {
            giftOrderService.updateGiftOrderStatus(orderId, "CANCELLED");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đơn quà tặng đã được hủy");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
