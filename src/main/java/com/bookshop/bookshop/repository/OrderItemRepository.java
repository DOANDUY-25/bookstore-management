package com.bookshop.bookshop.repository;

import com.bookshop.bookshop.entity.Order;
import com.bookshop.bookshop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    
    /**
     * Find all order items for a specific order
     * @param order the order to find items for
     * @return list of order items
     */
    List<OrderItem> findByOrder(Order order);
}
