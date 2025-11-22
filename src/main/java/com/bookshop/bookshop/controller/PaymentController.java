package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.dto.PaymentDTO;
import com.bookshop.bookshop.entity.Order;
import com.bookshop.bookshop.entity.OrderGift;
import com.bookshop.bookshop.service.OrderService;
import com.bookshop.bookshop.service.GiftOrderService;
import com.bookshop.bookshop.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final GiftOrderService giftOrderService;

    public PaymentController(PaymentService paymentService, OrderService orderService, GiftOrderService giftOrderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.giftOrderService = giftOrderService;
    }
    
    @GetMapping("/order/{orderId}")
    public String showOrderPaymentForm(@PathVariable Integer orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        model.addAttribute("payment", new PaymentDTO());
        return "payment/order-payment";
    }
    
    @PostMapping("/order/{orderId}")
    public String processOrderPayment(@PathVariable Integer orderId,
                                     @Valid @ModelAttribute("payment") PaymentDTO dto,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        if (result.hasErrors()) {
            Order order = orderService.getOrderById(orderId);
            model.addAttribute("order", order);
            return "payment/order-payment";
        }
        
        try {
            paymentService.processOrderPayment(orderId, dto);
            redirectAttributes.addFlashAttribute("success", "Payment processed successfully");
            return "redirect:/orders/" + orderId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/payment/order/" + orderId;
        }
    }
    
    @GetMapping("/gift-order/{orderGiftId}")
    public String showGiftOrderPaymentForm(@PathVariable Integer orderGiftId, Model model) {
        OrderGift orderGift = giftOrderService.getGiftOrderById(orderGiftId);
        model.addAttribute("orderGift", orderGift);
        model.addAttribute("payment", new PaymentDTO());
        return "payment/gift-order-payment";
    }
    
    @PostMapping("/gift-order/{orderGiftId}")
    public String processGiftOrderPayment(@PathVariable Integer orderGiftId,
                                         @Valid @ModelAttribute("payment") PaymentDTO dto,
                                         BindingResult result,
                                         RedirectAttributes redirectAttributes,
                                         Model model) {
        if (result.hasErrors()) {
            OrderGift orderGift = giftOrderService.getGiftOrderById(orderGiftId);
            model.addAttribute("orderGift", orderGift);
            return "payment/gift-order-payment";
        }
        
        try {
            paymentService.processGiftOrderPayment(orderGiftId, dto);
            redirectAttributes.addFlashAttribute("success", "Payment processed successfully");
            return "redirect:/gift-orders/" + orderGiftId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/payment/gift-order/" + orderGiftId;
        }
    }
    
    // COD Payment API
    @PostMapping("/cod/{orderId}")
    @ResponseBody
    public ResponseEntity<?> processCODPayment(@PathVariable Integer orderId) {
        try {
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setPaymentMethod("COD");
            paymentDTO.setTransactionId("COD-" + System.currentTimeMillis());
            
            paymentService.processOrderPayment(orderId, paymentDTO);
            
            // Update order status to CONFIRMED
            orderService.updateOrderStatus(orderId, "CONFIRMED");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đơn hàng đã được xác nhận. Thanh toán khi nhận hàng.");
            response.put("paymentMethod", "COD");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // VNPay Create QR Code API (Fake)
    @PostMapping("/vnpay/create-qr/{orderId}")
    @ResponseBody
    public ResponseEntity<?> createVNPayQR(@PathVariable Integer orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            
            // Generate fake transaction ID
            String transactionId = "VNP" + System.currentTimeMillis();
            
            // Create QR data (fake VNPay QR format)
            String qrData = String.format(
                "VNPay|%s|%s|%s|https://vnshop.vn",
                transactionId,
                order.getOrderId(),
                order.getTotalAmount()
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("orderId", orderId);
            response.put("amount", order.getTotalAmount());
            response.put("transactionId", transactionId);
            response.put("qrData", qrData);
            response.put("message", "QR Code đã được tạo");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // VNPay Confirm Payment API (Fake)
    @PostMapping("/vnpay/confirm/{orderId}")
    @ResponseBody
    public ResponseEntity<?> confirmVNPayPayment(@PathVariable Integer orderId, @RequestBody Map<String, Object> paymentData) {
        try {
            String transactionId = (String) paymentData.get("transactionId");
            
            if (transactionId == null || transactionId.trim().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Mã giao dịch không hợp lệ");
                return ResponseEntity.badRequest().body(error);
            }
            
            // Fake VNPay payment confirmation
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setPaymentMethod("VNPAY");
            paymentDTO.setTransactionId(transactionId);
            
            paymentService.processOrderPayment(orderId, paymentDTO);
            
            // Update order status to CONFIRMED
            orderService.updateOrderStatus(orderId, "CONFIRMED");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Thanh toán VNPay thành công");
            response.put("paymentMethod", "VNPAY");
            response.put("transactionId", transactionId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // Gift Order COD Payment API
    @PostMapping("/gift-order/cod/{orderGiftId}")
    @ResponseBody
    public ResponseEntity<?> processGiftOrderCODPayment(@PathVariable Integer orderGiftId) {
        try {
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setPaymentMethod("COD");
            paymentDTO.setTransactionId("COD-GIFT-" + System.currentTimeMillis());
            
            paymentService.processGiftOrderPayment(orderGiftId, paymentDTO);
            
            // Update gift order status to PAID
            giftOrderService.updateGiftOrderStatus(orderGiftId, "PAID");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đơn quà tặng đã được xác nhận. Thanh toán khi nhận hàng.");
            response.put("paymentMethod", "COD");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // Gift Order VNPay Create QR Code API (Fake)
    @PostMapping("/gift-order/vnpay/create-qr/{orderGiftId}")
    @ResponseBody
    public ResponseEntity<?> createGiftOrderVNPayQR(@PathVariable Integer orderGiftId) {
        try {
            OrderGift orderGift = giftOrderService.getGiftOrderById(orderGiftId);
            
            // Generate fake transaction ID
            String transactionId = "VNP-GIFT-" + System.currentTimeMillis();
            
            // Create QR data (fake VNPay QR format)
            String qrData = String.format(
                "VNPay|%s|GIFT-%s|%s|https://vnshop.vn",
                transactionId,
                orderGift.getOrderGiftId(),
                orderGift.getTotalAmount()
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("orderGiftId", orderGiftId);
            response.put("amount", orderGift.getTotalAmount());
            response.put("transactionId", transactionId);
            response.put("qrData", qrData);
            response.put("message", "QR Code đã được tạo");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // Gift Order VNPay Confirm Payment API (Fake)
    @PostMapping("/gift-order/vnpay/confirm/{orderGiftId}")
    @ResponseBody
    public ResponseEntity<?> confirmGiftOrderVNPayPayment(@PathVariable Integer orderGiftId, @RequestBody Map<String, Object> paymentData) {
        try {
            String transactionId = (String) paymentData.get("transactionId");
            
            if (transactionId == null || transactionId.trim().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Mã giao dịch không hợp lệ");
                return ResponseEntity.badRequest().body(error);
            }
            
            // Fake VNPay payment confirmation
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setPaymentMethod("VNPAY");
            paymentDTO.setTransactionId(transactionId);
            
            paymentService.processGiftOrderPayment(orderGiftId, paymentDTO);
            
            // Update gift order status to PAID
            giftOrderService.updateGiftOrderStatus(orderGiftId, "PAID");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Thanh toán VNPay thành công");
            response.put("paymentMethod", "VNPAY");
            response.put("transactionId", transactionId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
