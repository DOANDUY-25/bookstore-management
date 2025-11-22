package com.bookshop.bookshop.service;

import com.bookshop.bookshop.dto.GiftOrderCreateDTO;
import com.bookshop.bookshop.entity.OrderGift;

import java.util.List;

public interface GiftOrderService {
    
    OrderGift createGiftOrder(Integer userId, GiftOrderCreateDTO dto);
    
    OrderGift getGiftOrderById(Integer orderGiftId);
    
    List<OrderGift> getGiftOrdersByUser(Integer userId);
    
    List<OrderGift> getAllGiftOrders();
    
    OrderGift updateGiftOrderStatus(Integer orderGiftId, String status);
}
