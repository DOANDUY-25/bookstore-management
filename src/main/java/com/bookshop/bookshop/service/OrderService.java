package com.bookshop.bookshop.service;

import com.bookshop.bookshop.dto.OrderCreateDTO;
import com.bookshop.bookshop.entity.Order;

import java.util.List;

public interface OrderService {
    
    Order createOrder(Integer userId, OrderCreateDTO dto);
    
    Order getOrderById(Integer orderId);
    
    List<Order> getOrdersByUser(Integer userId);
    
    List<Order> getAllOrders();
    
    Order updateOrderStatus(Integer orderId, String status);
    
    void cancelOrder(Integer orderId);
}
