package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.entity.OrderGift;
import com.bookshop.bookshop.service.GiftOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/gift-orders")
public class AdminGiftOrderController {
    
    private final GiftOrderService giftOrderService;

    public AdminGiftOrderController(GiftOrderService giftOrderService) {
        this.giftOrderService = giftOrderService;
    }
    
    @GetMapping
    public String listGiftOrders(Model model) {
        List<OrderGift> giftOrders = giftOrderService.getAllGiftOrders();
        model.addAttribute("giftOrders", giftOrders);
        return "admin/gift-orders/list";
    }
    
    @GetMapping("/{id}")
    public String viewGiftOrderDetails(@PathVariable Integer id, Model model) {
        OrderGift orderGift = giftOrderService.getGiftOrderById(id);
        model.addAttribute("orderGift", orderGift);
        return "admin/gift-orders/details";
    }
    
    @PostMapping("/{id}/update-status")
    public String updateGiftOrderStatus(@PathVariable Integer id,
                                       @RequestParam String status,
                                       RedirectAttributes redirectAttributes) {
        try {
            giftOrderService.updateGiftOrderStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Gift order status updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/gift-orders/" + id;
    }
}
