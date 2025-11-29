package com.bookshop.bookshop.controller.api;

import com.bookshop.bookshop.entity.OrderGift;
import com.bookshop.bookshop.service.GiftOrderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/public/gift-orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftOrderApiController {
    
    private final GiftOrderService giftOrderService;

    public GiftOrderApiController(GiftOrderService giftOrderService) {
        this.giftOrderService = giftOrderService;
    }
    
    @GetMapping
    public ResponseEntity<List<OrderGift>> getAllGiftOrders() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(giftOrderService.getAllGiftOrders());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderGift> getGiftOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(giftOrderService.getGiftOrderById(id));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderGift>> getGiftOrdersByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(giftOrderService.getGiftOrdersByUser(userId));
    }
}
